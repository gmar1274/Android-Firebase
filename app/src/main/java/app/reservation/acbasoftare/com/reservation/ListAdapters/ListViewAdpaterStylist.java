package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MessagingActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.CircleImage;
import app.reservation.acbasoftare.com.reservation.App_Objects.CustomFBProfile;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessagingUserMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 5/31/17.
 */
public class ListViewAdpaterStylist extends ArrayAdapter<Stylist> {
        private HashMap<String, Boolean> loaded;
        private CustomFBProfile profile;
        private HashMap<String, String> stylist_bitmaps;
        private MainActivity ma;
        //private ArrayList<Stylist> list;
        public ListViewAdpaterStylist(MainActivity ma, int list_view_live_feed, ArrayList<Stylist> values, CustomFBProfile profile, HashMap<String, String> stylist_bitmaps) {
            super(ma, list_view_live_feed,values);

            this.ma = ma;
            this.loaded = new HashMap<>();
            this.profile = profile;
            this.stylist_bitmaps = stylist_bitmaps;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final Stylist s = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);
            RadioButton r = (RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
            r.setChecked(position == ma.stylist_position);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ma.stylist_position = (Integer) view.getTag();
                    notifyDataSetChanged();
                    //Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                }
            });
            TextView tv_stylist = (TextView) convertView.findViewById(R.id.textView_stylist_lv);
            tv_stylist.setText(s.getName().toUpperCase());
            TextView tv_waiting = (TextView) convertView.findViewById(R.id.tv_waiting_lv);
            tv_waiting.setText(String.valueOf(s.getWait()));
            TextView tv_approx_wait = (TextView) convertView.findViewById(R.id.textView_aprox_wait_lv);
            tv_approx_wait.setText(Utils.calculateWait(s.getPsuedo_wait()));
            TextView tv_readyby = (TextView) convertView.findViewById(R.id.textView_readyby_lv);
            tv_readyby.setText(Utils.calculateTimeReady(s.getPsuedo_wait()));
            ///////
            TextView tv3 = (TextView) convertView.findViewById(R.id.textView3);
            TextView tv4 = (TextView) convertView.findViewById(R.id.textView4);
            TextView tv5 = (TextView) convertView.findViewById(R.id.textView5);
            TextView tv6 = (TextView) convertView.findViewById(R.id.textView6);
            Button msg_btn = (Button) convertView.findViewById(R.id.msg_sty_btn);//messege stylist
            if (s.getId().equalsIgnoreCase("-1")) {
                // Log.e("Button hidden", "id store");
                msg_btn.setVisibility(View.GONE);
            }///if this is the store then dont display button to message
            msg_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.e("Button", "Button clicked");
                    if (profile == null) {
                        Toast.makeText(getContext(), "Must be logged in to use this feature.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Bitmap sty = Utils.decodeFileToBitmap(getContext(),stylist_bitmaps.get(s.getId()));
                    if(sty == null){
                        sty = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.acba);
                    }
                    // Uri user_uri = profile.getUri();
                    // ImageView iv = new ImageView(getContext());
                    // iv.setImageURI(user_uri);
                    // Bitmap user = iv.getDrawingCache();//MediaStore.Images.Media.getBitmap(ma.getContentResolver(), user_uri);
                    // Utils.saveFileToDisk(sty, Utils.SELECTED_USER);
                    // Utils.saveFileToDisk(user, Utils.USER);
                    Intent i = new Intent(getContext(), MessagingActivity.class);
                    FirebaseMessagingUserMetaData userMeta = new FirebaseMessagingUserMetaData(profile);
                    FirebaseMessagingUserMetaData selectedUser = new FirebaseMessagingUserMetaData(ma.store, s);
                    i.putExtra(Utils.USER, userMeta);
                    i.putExtra(Utils.SELECTED_USER, selectedUser);
                    i.putExtra(Utils.USER_BITMAP_LOCATION,ma.stylist_bitmaps.get(ma.user_fb_profile.getId()));
                    i.putExtra(Utils.SELECTED_USER_BITMAP_LOCATION,selectedUser.getId());
                    getContext().startActivity(i);
                }
            });
            //Bitmap myBitmap = BitmapFactory.decodeFile("\\res\\drawable\\logo.png");
            QuickContactBadge iv = (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge);
            //if(s.getImage_bytes() == null){
            //iv.setImageDrawable(R.drawable.acba);//Utils.resize(rootView.getContext(),rootView.getResources().getDrawable(R.drawable.acba),50,50));
            // }else {
            //  if (stylist_bitmaps != null && stylist_bitmaps.size()>=position+1 ){

            Bitmap sty_bm = Utils.decodeFileToBitmap(getContext(),stylist_bitmaps.get(s.getId()));
            if(sty_bm==null){
                //dont do anything...or actually default pic
                sty_bm = Utils.setDefaultBitmap(this.getContext());
                iv.setImageDrawable(new CircleImage(sty_bm));
            }else{
                iv.setImageDrawable(new CircleImage(sty_bm)); // iv.setImageBitmap(); // iv.setImageBitmap(Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())));
            }
               if (position == 0) {
                iv.assignContactFromPhone(ma.store_list.get(ma.selectedPosition).getPhone(), true);
            } else {
                iv.assignContactFromPhone(s.getPhone(), true);
                ////Update whwn the Stylist becomes ACTIVE OR INACTIVE...
                if (!loaded.containsKey(s.getId())) {
                    /////////////////////////add listener to stylists
                    //I CAN SEE A BUG WHEN USER DELETES A STYLISTS DURONG PEAK HOURS
                    // MIGHT STILL BE LOADED IN APP IF SOMEONE CLICKS to purchase etc...
                    DatabaseReference sty = FirebaseDatabase.getInstance().getReference().child("stylists/" + ma.store.getStore_number() + "/" + s.getId());
                    sty.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Log.e("in LA:::.", dataSnapshot.getValue()+"");

                            if (dataSnapshot.getValue() == null) {
                                remove(s);
                                stylist_bitmaps.remove(s.getId());
                                notifyDataSetChanged();
                                return;
                            }
                            Stylist sty = dataSnapshot.getValue(Stylist.class);
                            //remove(s);
                            // insert(sty,position);
                            s.setAvailable(sty.isAvailable());
                            loaded.put(sty.getId(), true);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }
            //}
//}
            iv.setMode(ContactsContract.QuickContact.MODE_MEDIUM);
            // iv.setVisibility(View.VISIBLE);
                /*r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer) view.getTag();
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                    }
                });*/
            //Log.e("Styyy:",s.isAvailable()+" <<>>> "+s);
            if (!s.isAvailable()) {
                r.setEnabled(false);
                convertView.setOnClickListener(null);
                convertView.setEnabled(false);
            }
            return convertView;
        }
    }
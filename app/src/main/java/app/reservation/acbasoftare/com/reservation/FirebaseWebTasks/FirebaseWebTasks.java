package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import app.reservation.acbasoftare.com.reservation.App_Activity.InStoreTicketReservationActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.stylist_bitmaps;
import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.bitmaps;


/**
 * Created by user on 1/16/17.
 */
public class FirebaseWebTasks {
    /**
     * This method uses TicketSceenActivity to save the image under root/store_id/images/stylists/filename
     *
     * @param bitmap
     * @param filename
     */
    public static void uploadImage(final Activity a , Bitmap bitmap, String filename) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        Bitmap bit=bitmap;
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] arr=stream.toByteArray();
        TicketScreenActivity tsa = (TicketScreenActivity) a;
        StorageReference ref= tsa.mStorageRef.child(tsa.store.getStore_number() + "/images/stylists/" + filename);//ticketScreenActivity
        UploadTask up=ref.putBytes(arr);
        up.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                //showToast(a,"Imaged failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Storage File: ", "Success on upload");
                Toast.makeText(a,"Image successfully uploaded!",Toast.LENGTH_LONG).show(); // showToast(a,"Imaged success on upload");  //Uri downloadURL = taskSnapshot.getDownloadUrl();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(a,"Image failed uploaded...",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This methods will popluate the lists of both stylists and bitmaps. Bitmpas will contain the pictures from firebase storeage and
     * Stylist will contain the stylists from the store gathered by the query from firebase database.
     * @param store
     * @param list_stylist
     * @param pd
     */
    public static void downloadImages(final TicketScreenActivity tsa, final FirebaseStore store, final ArrayList<Stylist>list_stylist, final ProgressDialog pd){

       // final TicketScreenActivity tsa = (TicketScreenActivity) pd.getContext();
        for(final Stylist s : list_stylist ) {
           //Log.e("FIREBASE IMAGES STYLIS:",s.getId()+" ID AND NAME: "+s.getName());
            String name = s.getId();
           // if(name.equals("-1")){name = "-1.png";}
            StorageReference sr = tsa.mStorageRef.child(store.getPhone()+ "/images/stylists/"+name);
            sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("OnSuccessImage","image on success");
                    Bitmap b = Utils.convertBytesToBitmap(bytes);//Utils.convertToByteArray(s.getImage_bytes())); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                    bitmaps.add(b);
                    bytes=null;
                    if(bitmaps.size() == list_stylist.size()){
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                   Log.e("ERROR","IMAGE ERROR"); // Handle any errors
                    exception.printStackTrace();
                   pd.dismiss();
                }
            });
        }

    }

    /**
     * This method fetches the images and displays them on the View.
     * @param store
     * @param list_stylist
     * @param pd
     */
    public static void downloadImages(final MainActivity ma , final FirebaseStore store, final ArrayList<Stylist>list_stylist, final ProgressDialog pd){

        // final TicketScreenActivity tsa = (TicketScreenActivity) pd.getContext();
        for(final Stylist s : list_stylist ) {
            //Log.e("FIREBASE IMAGES STYLIS:",s.getId()+" ID AND NAME: "+s.getName());
            String name = s.getId();
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(store.getPhone()+ "/images/stylists/"+name);
            sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("OnSuccessImage","image on success");
                   // s.setImage_bytes(Utils.convertToString(bytes));
                    //bytes=null;
                    //list_stylist.add(s);
                    Bitmap b = Utils.convertBytesToBitmap(bytes); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                    bytes=null;
                    if(stylist_bitmaps==null){stylist_bitmaps = new ArrayList<Bitmap>();}
                    stylist_bitmaps.add(b);
                    if(stylist_bitmaps.size() == list_stylist.size()){
                        //store.determineCurrentTicketAfterFirebaseLoads();
                        ma.updateStore(store);
                        ma.initializeStylists(list_stylist,stylist_bitmaps);
                        Log.e("FINISH: ","Finished download images");
                        pd.dismiss();
                        ProgressBar pb = (ProgressBar)ma.rootView_LiveTab.findViewById(R.id.progressBar_live_feed);
                        pb.setVisibility(View.GONE);
                    }
                    //not sure store gets updated will delete the use even though its stored in array cant useon the change but its ok because i can set the stylist list to the list for store
                    // pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    stylist_bitmaps = null;
                    //ma.initializeStylists(ma.stylists_list,null);
                    Log.e("ERROR","IMAGE ERROR"); // Handle any errors
                    exception.printStackTrace();
                    pd.dismiss();
                }
            });
        }

    }
    public static class ListViewAdpaterStylist extends ArrayAdapter<Stylist> {
        private MainActivity ma;
        public ListViewAdpaterStylist(MainActivity ma, int list_view_live_feed, ArrayList<Stylist> values) {
            super(ma, list_view_live_feed, values);
            this.ma = ma;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            Stylist s = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);
            RadioButton r = (RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
            r.setChecked(position ==  ma.stylist_position);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ma.stylist_position = (Integer) view.getTag();
                    notifyDataSetChanged();
                    //Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                }
            });
            TextView tv_stylist=(TextView)convertView.findViewById(R.id.textView_stylist_lv);
            tv_stylist.setText(s.getName().toUpperCase());
            TextView tv_waiting=(TextView)convertView.findViewById(R.id.tv_waiting_lv);
            tv_waiting.setText(String.valueOf(s.getWait()));
            TextView tv_approx_wait=(TextView)convertView.findViewById(R.id.textView_aprox_wait_lv);
            tv_approx_wait.setText(Utils.calculateWait(s.getWait()));
            TextView tv_readyby=(TextView)convertView.findViewById(R.id.textView_readyby_lv);
            tv_readyby.setText(Utils.calculateTimeReady(s.getWait()));
            ///////
            TextView tv3=(TextView)convertView.findViewById(R.id.textView3);
            TextView tv4=(TextView)convertView.findViewById(R.id.textView4);
            TextView tv5=(TextView)convertView.findViewById(R.id.textView5);
            TextView tv6=(TextView)convertView.findViewById(R.id.textView6);

            //Bitmap myBitmap = BitmapFactory.decodeFile("\\res\\drawable\\logo.png");
            QuickContactBadge iv = (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge);
            //if(s.getImage_bytes() == null){
                //iv.setImageDrawable(R.drawable.acba);//Utils.resize(rootView.getContext(),rootView.getResources().getDrawable(R.drawable.acba),50,50));
           // }else {
                if (stylist_bitmaps != null && stylist_bitmaps.size()>=position+1 ){
                    iv.setImageBitmap(stylist_bitmaps.get(position)); // iv.setImageBitmap(Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())));
                    if(position==0){
                        iv.assignContactFromPhone(ma.store_list.get(ma.selectedPosition).getPhone(),true);
                    }else{
                        iv.assignContactFromPhone(s.getPhone(),true);
                    }
                }
            //}
            iv.setMode(ContactsContract.QuickContact.MODE_LARGE);
            // iv.setVisibility(View.VISIBLE);
                /*r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer) view.getTag();
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                    }
                });*/
            return convertView;
        }
    }
}

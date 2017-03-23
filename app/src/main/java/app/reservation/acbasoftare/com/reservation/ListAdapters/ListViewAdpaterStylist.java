package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.MessagingActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 2017-02-16.
 * used for MAIN TICKET INSTORE AND TICKETACTIVITY self kiosk.....main display
 */
public class ListViewAdpaterStylist  extends ArrayAdapter<Stylist> {
    private int indexSelected;
    private HashMap<String, Bitmap> bitmapHashMap;
    private UserMessageMetaData user;
    private Profile profile;
    public ListViewAdpaterStylist(Activity act, int list_view_live_feed, ArrayList<Stylist> values, HashMap<String, Bitmap> bitmapHashMap, UserMessageMetaData user, Profile prof) {
        super(act, list_view_live_feed, values);
        this.indexSelected = 0;
        this.bitmapHashMap = bitmapHashMap;
        this.user = user;
        this.profile = prof;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Stylist s = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);
        RadioButton r = (RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
        r.setChecked(position == indexSelected);
        r.setTag(position);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexSelected = (Integer) view.getTag();
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

        QuickContactBadge iv = (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge);

        iv.setImageBitmap(bitmapHashMap.get(s.getId())); // iv.setImageBitmap(Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())));
        iv.assignContactFromPhone(s.getPhone(), true);
        iv.setMode(ContactsContract.QuickContact.MODE_LARGE);

        Button msg_btn = (Button) convertView.findViewById(R.id.msg_sty_btn);
        msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap sty = bitmapHashMap.get(s.getId());
                Uri user_uri = profile.getLinkUri();
                ImageView iv = new ImageView(getContext());
                iv.setImageURI(user_uri);
                Bitmap user = iv.getDrawingCache();//MediaStore.Images.Media.getBitmap(ma.getContentResolver(), user_uri);
                Utils.saveFileToDisk(sty,"sty");
                Utils.saveFileToDisk(user,"user");


                Intent i = new Intent(getContext(), MessagingActivity.class);
                i.putExtra("UserMessageMetaData",user);
                getContext().startActivity(i);
            }
        });

        return convertView;
    }
}
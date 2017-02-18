package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.stylist_bitmaps;

/**
 * Created by user on 2017-02-16.
 * used for MAIN TICKET INSTORE AND TICKETACTIVITY self kiosk.....main display
 */
public class ListViewAdpaterStylist  extends ArrayAdapter<Stylist> {
    private int indexSelected;
    private HashMap<String, Bitmap> bitmapHashMap;

    public ListViewAdpaterStylist(Activity act, int list_view_live_feed, ArrayList<Stylist> values, HashMap<String, Bitmap> bitmapHashMap) {
        super(act, list_view_live_feed, values);
        this.indexSelected = 0;
        this.bitmapHashMap = bitmapHashMap;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Stylist s = getItem(position);
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

        return convertView;
    }
}
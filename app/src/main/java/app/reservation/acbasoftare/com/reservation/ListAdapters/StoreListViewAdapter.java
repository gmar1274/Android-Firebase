package app.reservation.acbasoftare.com.reservation.ListAdapters;

/**
 * Created by user on 5/14/17.
 */

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * This is for Store list.
 * ArrayAdpater for stores.
 */
public  class StoreListViewAdapter extends ArrayAdapter<FirebaseStore> {
    private MainActivity ma;
    private ArrayList<FirebaseStore> list;
    private int current_index;

    public StoreListViewAdapter(MainActivity ma, ArrayList<FirebaseStore> values) {
        super(ma, R.layout.list_view_layout, values);
        this.list = values;
        this.ma = ma;
        this.current_index = 0;
    }

    /**
     * Implement getView method for customizing row of list view.
     * this method creates store_list single view that correponds to the data being passed in.
     * get the STORE data by getItem(position)
     */
    public View getView(final int position_item, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        // Creating store_list view of row.
        final FirebaseStore store = getItem(position_item);
        View rowView = inflater.inflate(R.layout.list_view_layout, parent, false);
        RadioButton r = (RadioButton) rowView.findViewById(R.id.radio_button);
        double miles_away = store.getMiles_away();
        DecimalFormat df = new DecimalFormat("0.##");

        // r.setText("Shop: " + store.getName() + "\n" + "Miles away: " + df.format(miles_away) + "\nHours: " + store.formatHoursTo12hours() + "\n" + store.displayIsAvailable());//+"\n" + "Address: " + store.getAddress().toUpperCase() + "\n" + store.getCitystate().toUpperCase());
        TextView t = (TextView) rowView.findViewById(R.id.shopNameTextView);
        t.setText(store.getName());
        TextView tt = (TextView) rowView.findViewById(R.id.distanceTextView);
        tt.setText(df.format(miles_away)+" mi");
        TextView ttt = (TextView) rowView.findViewById(R.id.hoursTextView);
        ttt.setText(store.formatHoursTo12hours());

        r.setChecked(position_item == ma.selectedPosition);
        r.setTag(position_item);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position_item != ma.selectedPosition) {//new store...go get the stylists from that db
                    ma.stylists_list = null;//new store null stylists
                }
                int LAST_PICKED = ma.selectedPosition;
                ma.selectedPosition = (Integer) view.getTag();
                if (ma.selectedPosition != LAST_PICKED) {/////different SHOP delete bitmaps saved when loaded
                    // Log.e("DIFF CHOICE::","sp: "+ma.selectedPosition+" <> lastpicked: "+LAST_PICKED);
                    // ma.STYLIST_BITMAPS_LOADED = false;
                    if (ma.stylists_list != null) ma.stylists_list.clear();
                    if (ma.stylist_bitmaps != null) {
                        deleteFiles(ma.stylist_bitmaps);
                        ma.stylist_bitmaps.clear();
                    }
                    ma.stylist_bitmaps = null;
                    ma.stylists_list = null;
                }
                current_index = position_item;
                notifyDataSetChanged();//updates the button click isset
                LatLng myLoc = store.getLocation();
                // Updates the location and zoom of the MapView
                com.google.android.gms.maps.model.LatLng l = new com.google.android.gms.maps.model.LatLng(myLoc.latitude, myLoc.longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(l, 14);//20 is closeset 5 is largest
                ma.google_map.animateCamera(cameraUpdate);
                ma.mapview.onResume();
            }

            /**
             * Helper method to delete temp files created wen downloading from tab2 of a shop.
             * @param stylist_bitmaps
             */
            private void deleteFiles(HashMap<String, String> stylist_bitmaps) {
                for(String id:stylist_bitmaps.keySet()){
                    File temp = new File(stylist_bitmaps.get(id));
                    if(temp.exists()){
                        if(temp.delete()){
                            Log.e("delete:","temp successfully deleted");
                        }else{
                            Log.e("Errr","no delete");
                        }
                    }
                }
            }
        });
        return rowView;
    }


    public int getPositionFromStoreID(String  google_id) {
        return list.indexOf(new FirebaseStore(google_id));
    }
    public List<FirebaseStore> getList(){return this.list;}

    public void sortByDistance() {
        Collections.sort(list);
    }
    public int getCurrentPosition(){
        return this.current_index;
    }
    public void sortByName() {
        Collections.sort(list, new Comparator<FirebaseStore>() {
            @Override
            public int compare(FirebaseStore s, FirebaseStore ss) {
                return s.getName().compareTo(ss.getName());
            }
        });
    }

    public void selectRadioButtonFor(int pos) {
        current_index=pos;
        ma.selectedPosition=pos;
        notifyDataSetChanged();
    }
}

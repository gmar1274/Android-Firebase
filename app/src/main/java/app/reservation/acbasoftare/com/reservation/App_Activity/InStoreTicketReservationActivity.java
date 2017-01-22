package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.listView;
import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.tickets;

public class InStoreTicketReservationActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Bitmap> bitmaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store_ticket_reservation);
        Button cancel = (Button) this.findViewById(R.id.cancelBTN);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        Button ticket = (Button) this.findViewById(R.id.reserveBTN);
        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTicket();        //send a new entry to firebase url: root/ticket/store_id/TICKET{JSON}
            }
        });
        loadGUI();
    }

    /**
     * Display the stylists in a ListView
     */
    private void loadGUI() {
        if (bitmaps == null) {
            this.bitmaps = new ArrayList<>();
        }
        lv = (ListView) this.findViewById(R.id.choose_stylist_listview);
        if (lv.getAdapter() == null) {
            ListAdapter la = new ListViewAdpaterStylist(this.getApplicationContext(), R.layout.list_view_live_feed, TicketScreenActivity.stylist_list);
            lv.setAdapter(la);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int position, long l) {
                // Log.e("CLICKED IN LISTVIEW::", "POSITION:: " + position);//working correctl because od escendats xml
                // Toast.makeText(InStoreTicketReservationActivity.this.getApplicationContext(), "Position: " + position, Toast.LENGTH_LONG).show();
                TicketScreenActivity.stylist_postion = position;
                //
                Stylist s = TicketScreenActivity.stylist_list.get(position);
                // updateView(position);
                ((ListViewAdpaterStylist) adapterView.getAdapter()).notifyDataSetChanged();

                Toast.makeText(InStoreTicketReservationActivity.this, s.getName() + " selected.", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /**
     * Send ticket firebase
     * Update thhe current_ticket property in Store object in Firebase..
     */
    private void sendTicket() {
        Store store = TicketScreenActivity.store;
        Stylist s = TicketScreenActivity.stylist_list.get(TicketScreenActivity.stylist_postion);

        store.updateStylistWait(s.getId());

        Map<String, Object> map = new HashMap<>();
        map.put(String.valueOf(store.getStore_number()), store);

        DatabaseReference ref = TicketScreenActivity.myRef;
        ref.setValue(map);
        store.setStylistList(TicketScreenActivity.stylist_list);
        TicketScreenActivity.stylist_list = (ArrayList<Stylist>) store.getStylistListFirebaseFormat(); //update styl array
        Toast.makeText(this,"Ticket received. Thank you!",Toast.LENGTH_LONG).show();
        Ticket t = new Ticket(store.getPhone()+"",(s.getWait()+1)+"","???", s.getName(),null);
        tickets.add(t);
        ((ArrayAdapter<Ticket>)listView.getAdapter()).notifyDataSetChanged();
        this.finish();
    }

    private void goBack() {
        this.finish();
    }

    private class ListViewAdpaterStylist extends ArrayAdapter<Stylist> {

        // private boolean saveImage=false;
        public ListViewAdpaterStylist(Context c, int list_view_live_feed, ArrayList<Stylist> values) {
            super(c, list_view_live_feed, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            //View rowView = inflater.inflate(R.layout.list_view_live_feed, parent, false);
            Stylist s = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);
            final RadioButton r = (RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
            //r.setText("Stylist: " + s.getName() + "\n" + "Waiting: " + s.getWait()+"\nApprox. Wait: "+Utils.calculateWait(s.getWait())+"\nEstimated Time: "+ Utils.calculateTimeReady(s.getWait()));
            r.setChecked(position == TicketScreenActivity.stylist_postion);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    r.setChecked(true);
                    //updateView(position);
                    notifyDataSetChanged();
                }
            });
            //was a click listener
            TextView tv_stylist = (TextView) convertView.findViewById(R.id.textView_stylist_lv);
            tv_stylist.setText(s.getName().toUpperCase());
            //setListener(tv_stylist, r);
            TextView tv_waiting = (TextView) convertView.findViewById(R.id.tv_waiting_lv);
            tv_waiting.setText("" + s.getWait());
            //setListener(tv_waiting, r);
            TextView tv_approx_wait = (TextView) convertView.findViewById(R.id.textView_aprox_wait_lv);
            tv_approx_wait.setText(Utils.calculateWait(s.getWait()));
            //setListener(tv_approx_wait, r);
            TextView tv_readyby = (TextView) convertView.findViewById(R.id.textView_readyby_lv);
            tv_readyby.setText(Utils.calculateTimeReady(s.getWait()));
            //setListener(tv_readyby, r);
            ///////
            TextView tv3 = (TextView) convertView.findViewById(R.id.textView3);
            //setListener(tv3, r);
            TextView tv4 = (TextView) convertView.findViewById(R.id.textView4);
            //setListener(tv4, r);
            TextView tv5 = (TextView) convertView.findViewById(R.id.textView5);
            // setListener(tv5, r);
            TextView tv6 = (TextView) convertView.findViewById(R.id.textView6);
            // setListener(tv6, r);
            //Bitmap myBitmap = BitmapFactory.decodeFile("\\res\\drawable\\logo.png");
            ImageView iv = (ImageView) convertView.findViewById(R.id.quickContactBadge);
            if (s.getImage_bytes() == null) {
                //iv.setImageDrawable(R.drawable.acba);//Utils.resize(rootView.getContext(),rootView.getResources().getDrawable(R.drawable.acba),50,50));
            } else {
                if (iv.getDrawable() == null) {
                    //Bitmap b = Utils.convertBytesToBitmap(s.getImageBytes());
                    if( position+1 > bitmaps.size()) {
                        Bitmap b = Utils.convertBytesToBitmap(s.getImage_bytes()); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                        iv.setImageBitmap(b);
                        bitmaps.add(b);
                    }else{
                        iv.setImageBitmap(bitmaps.get(position));
                    }
                }
            }
            // iv.assignContactFromPhone(s.getPhone(), true);
            //iv.setMode(ContactsContract.QuickContact.MODE_LARGE);

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

       /* private void setListener(TextView tv, final RadioButton rb) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TicketScreenActivity.stylist_postion = (Integer) rb.getTag();
                    rb.setChecked(true);
                    notifyDataSetChanged();

                }
            });*/
    }


}
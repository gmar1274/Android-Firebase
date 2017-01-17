package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

public class InStoreTicketReservationActivity extends AppCompatActivity {
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store_ticket_reservation);
        Button cancel=(Button) this.findViewById(R.id.cancelBTN);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        Button ticket=(Button) this.findViewById(R.id.reserveBTN);
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
        lv=(ListView) this.findViewById(R.id.choose_stylist_listview);

        ListAdapter la=new ListViewAdpaterStylist(this.getApplicationContext(), R.layout.list_view_live_feed, TicketScreenActivity.stylist_list);
        lv.setAdapter(la);
    }

    /**
     * Send ticket firebase
     */
    private void sendTicket() {
        //
    }

    private void goBack() {
        this.finish();
    }

    private class ListViewAdpaterStylist extends ArrayAdapter<Stylist> {
        public ListViewAdpaterStylist(Context c, int list_view_live_feed, ArrayList<Stylist> values) {
            super(c, list_view_live_feed, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            //View rowView = inflater.inflate(R.layout.list_view_live_feed, parent, false);
            Stylist s=getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            convertView=LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);

            RadioButton r=(RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
            //r.setText("Stylist: " + s.getName() + "\n" + "Waiting: " + s.getWait()+"\nApprox. Wait: "+Utils.calculateWait(s.getWait())+"\nEstimated Time: "+ Utils.calculateTimeReady(s.getWait()));
            r.setChecked(position == TicketScreenActivity.stylist_postion);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TicketScreenActivity.stylist_postion=(Integer) view.getTag();
                    notifyDataSetChanged();
                    //Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                }
            });
            TextView tv_stylist=(TextView) convertView.findViewById(R.id.textView_stylist_lv);
            tv_stylist.setText(s.getName().toUpperCase());
            setListener(tv_stylist, r);
            TextView tv_waiting=(TextView) convertView.findViewById(R.id.tv_waiting_lv);
            tv_waiting.setText("" + s.getWait());
            setListener(tv_waiting, r);
            TextView tv_approx_wait=(TextView) convertView.findViewById(R.id.textView_aprox_wait_lv);
            tv_approx_wait.setText(Utils.calculateWait(s.getWait()));
            setListener(tv_approx_wait, r);
            TextView tv_readyby=(TextView) convertView.findViewById(R.id.textView_readyby_lv);
            tv_readyby.setText(Utils.calculateTimeReady(s.getWait()));
            setListener(tv_readyby, r);
            ///////
            TextView tv3=(TextView) convertView.findViewById(R.id.textView3);
            setListener(tv3, r);
            TextView tv4=(TextView) convertView.findViewById(R.id.textView4);
            setListener(tv4, r);
            TextView tv5=(TextView) convertView.findViewById(R.id.textView5);
            setListener(tv5, r);
            TextView tv6=(TextView) convertView.findViewById(R.id.textView6);
            setListener(tv6, r);
            //Bitmap myBitmap = BitmapFactory.decodeFile("\\res\\drawable\\logo.png");
            QuickContactBadge iv=(QuickContactBadge) convertView.findViewById(R.id.quickContactBadge);
            if(s.getImage() == null) {
                //iv.setImageDrawable(R.drawable.acba);//Utils.resize(rootView.getContext(),rootView.getResources().getDrawable(R.drawable.acba),50,50));
            } else {
                iv.setImageBitmap(s.getImage());
            }
            iv.assignContactFromPhone(s.getPhone(), true);
            iv.setMode(ContactsContract.QuickContact.MODE_LARGE);

            return convertView;
        }

        private void setListener(TextView tv, final RadioButton rb) {
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TicketScreenActivity.stylist_postion = (Integer) rb.getTag();
                    rb.setChecked(true);
                    notifyDataSetChanged();

                }
            });
        }

    }
}
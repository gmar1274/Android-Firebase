package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;
import app.reservation.acbasoftare.com.reservation.WebTasks.SMSWebTask;

import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.bitmaps;

public class InStoreTicketReservationActivity extends AppCompatActivity {
    private ListView lv;
    private AutoCompleteTextView cust_name_textview;
    private EditText phone_textview;
    //private ArrayList<Stylist> stylist_list;
    //private ArrayList<Ticket> tickets;
    private int stylist_position;
    private ArrayList<Stylist> stylist_list;
    private FirebaseStore store;
    private HashMap<String,Stylist>sty_hm;
    private long current_ticket;
    //private Store store;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store_ticket_reservation);
       // FirebaseIntent fi = this.getIntent().getParcelableExtra("FirebaseIntent");
        Bundle b = this.getIntent().getExtras();
        this.stylist_list = b.getParcelableArrayList("stylist_list");
        // this.bitmaps = fi.getBitmaps() ;//this.getIntent().getParcelableArrayListExtra("bitmaps");
        //this.tickets = this.getIntent().getParcelableArrayListExtra("tickets");
        this.store =b.getParcelable("store");
        this.stylist_position = b.getInt("stylist_position");
        this.sty_hm = (HashMap<String, Stylist>) b.getSerializable("sty_hm");
        this.current_ticket = b.getLong("current_ticket");


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
                sendTicket();        //send a new entry to firebase url: root/ticket_number/store_id/TICKET{JSON}
            }
        });

        loadGUI();
    }

    /**
     * Display the stylists in a ListView
     */
    private void loadGUI() {
      //  loaded = false;


        lv = (ListView) this.findViewById(R.id.choose_stylist_listview);
        cust_name_textview = (AutoCompleteTextView) this.findViewById(R.id.customer_name_autocompletetextfield);
        phone_textview = (EditText) this.findViewById(R.id.phone_notification_autotextview);

         if (lv.getAdapter() == null) {
             ListViewAdpaterStylist la = new ListViewAdpaterStylist(this.getApplicationContext(), R.layout.list_view_live_feed, stylist_list);
            lv.setAdapter(la);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int position, long l) {
                // Log.e("CLICKED IN LISTVIEW::", "POSITION:: " + position);//working correctl because od escendats xml
                // Toast.makeText(InStoreTicketReservationActivity.this.getApplicationContext(), "Position: " + position, Toast.LENGTH_LONG).show();
                stylist_position = position;
                //
                Stylist s = stylist_list.get(position);
                // updateView(position);
                ((ListViewAdpaterStylist) adapterView.getAdapter()).notifyDataSetChanged();

                Toast.makeText(InStoreTicketReservationActivity.this, s.getName() + " selected.", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /**
     * FIREBASE URL: firebase-url-xxx/STORE_number/STORE_POJO_TO_JSON
     * Send ticket_number firebase
     * Update thhe current_ticket property in Store object in Firebase..
     * Update the tickets in firebase used for populating the the list view for android store app
     * Update the hashmap from the store property
     */
    private void sendTicket() {
        if( bitmaps==null || bitmaps.size() == 0 || stylist_list==null || stylist_list.size() == 0){return;}

        int pos = stylist_position;

        if(pos < 0){//no stylist has been selected
            showAlertDialog("Stylist not selected.","Please select a stylist.");
            return;
        }

        final FirebaseStore store = this.store;
        final Stylist s = stylist_list.get(pos);//get the selected stylist
        s.incrementWait();
       // store.updateStylistWait(s.getId());//increment wait of the selected stylist
        sty_hm.put(s.getId(),s);
        stylist_list.remove(pos);
        stylist_list.add(pos,s);//update the list with the current data for listview
        Toast.makeText(this,"Ticket received. Thank you!",Toast.LENGTH_SHORT).show();
        //store.incrementCurrentTicket();//update the CURRENT STORE TICKET
        String cust_name = this.cust_name_textview.getText().toString();//can optimize by creating Customer object....
        cust_name = cust_name.length()==0 ? "N/A":cust_name;
        final String cust_phone = this.phone_textview.getText().toString();

        //Create the ticket with Absolute Ticket#, Relative ticket#, cust_name, sty_id, sty_name, cust_phone...
      final Ticket t = new Ticket(current_ticket,(s.getWait())+"",cust_name,s.getId(), s.getName(),cust_phone);//create the ticket
        t.setReadyBy(s.getReadyBy());

      /*  //Create the ticket_number with Absolute Ticket#, Relative ticket_number#, cust_name, sty_id, sty_name, cust_phone...
      final Ticket t = new Ticket(store.getCurrent_ticket(),(s.getWait())+"",cust_name,s.getId(), s.getName(),cust_phone);//create the ticket_number
*/
        //to be stored---- ********NOTE****** store.getCurrent_ticket is invalid will be changed in transaction handler...
       // tickets.add(t);//add
       // store.addTicket(t);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets").child(String.valueOf(store.getStore_number()));//should be the correct store number path
        ref.runTransaction(new Transaction.Handler(){
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
               if(mutableData.getValue() != null) {//value in this case should be list of tickets
                   GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                   List<Ticket> curr_values = mutableData.getValue(gti);//get all the entries that are in this url_path
                   t.unique_id = curr_values.size()+1;
                   if (curr_values.contains(t)) {
                       t.unique_id += 1;//increment the store ticket_number
                       t.ticket_number = String.valueOf(Long.valueOf(t.ticket_number) + 1);//increment the next ticket_number waiting in line for stylist
                   }
                   curr_values.add(t);
                  //current_ticket = (t.unique_id);
                   mutableData.setValue(curr_values);
               }else{//there was no entries in the url so create new List<Ticket> with the first entry
                   t.unique_id = 1;///first entry
                   t.ticket_number = "1";
                   List<Ticket> l = new ArrayList<Ticket>();
                   l.add(t);
                   mutableData.setValue(l);
               }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                 resetStylistChoice();//deselect radio button
                //Log.e("ON CALL BACK TIKK: ",t+" phone: "+cust_phone +" rb: "+t.readyBy);
                if(t.phone != null && t.phone.length()>0) {

                    new SMSWebTask(store, t).execute();//send reciept
                }
                //updateHM(s,t);
            }
        });

        this.goBack();
    }
/*private void updateHM(final Stylist s, Ticket t){
Log.e("Wait: ",s.getWait()+"");
    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("stylists").child(String.valueOf(store.getStore_number())).child(s.getId());//.child(s.getId()).child("wait");//ref:root/store_number/tickets
    ref2.runTransaction(new Transaction.Handler() {
        @Override
        public Transaction.Result doTransaction(MutableData mutableData) {
            if(mutableData.getValue() != null){
               // Stylist ss = mutableData.getValue(Stylist.class);
                //if(ss.getWait() > )
                mutableData.setValue(s);
            }else{
                mutableData.setValue(s);
            }
            return Transaction.success(mutableData);
        }

        @Override
        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
            Log.e("ON UPDATE HM","updated...or at least in callback...");
        }
    });

}*/
    private void resetStylistChoice() {
        stylist_position = -1;
    }


    private void showAlertDialog(String title, String msg) {
        new AlertDialog.Builder(InStoreTicketReservationActivity.this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goBack() {
        Intent i = new Intent();
        Bundle b = new Bundle();
        b.putParcelable("store",store);
        b.putParcelableArrayList("stylist_list",stylist_list);
        b.putSerializable("sty_hm",sty_hm);
        b.putLong("current_ticket",current_ticket);
        i.putExtras(b);
        this.setResult(1,i);
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
            r.setChecked(position == stylist_position);
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
            tv_approx_wait.setText(Utils.calculateWait(s.getPsuedo_wait()));
            //setListener(tv_approx_wait, r);
            TextView tv_readyby = (TextView) convertView.findViewById(R.id.textView_readyby_lv);
            String readyBy = Utils.calculateTimeReady(s.getPsuedo_wait());
            s.setReadyBy(readyBy);
            tv_readyby.setText(readyBy);
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
            ImageView iv = (ImageView) convertView.findViewById(R.id.stylistBioImageView);
         //   if (s.getImage_bytes() == null) {
                //iv.setImageDrawable(R.drawable.acba);//Utils.resize(rootView.getContext(),rootView.getResources().getDrawable(R.drawable.acba),50,50));
           // } else {
                if (iv.getDrawable() == null) {
                    //Bitmap b = Utils.convertBytesToBitmap(s.getImageBytes());
                   /* if( position+1 > stylist_bitmaps.size()) {
                        Bitmap b = Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                        iv.setImageBitmap(b);
                        stylist_bitmaps.add(b);
                    }else{*/
                      // if(s.getImage_bytes() == null){}
                       // else if(loaded){
                            iv.setImageBitmap(TicketScreenActivity.bitmaps.get(s.getId()));
                       //}else{
                          /* Bitmap b = Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                           iv.setImageBitmap(b);
                           bitmaps.add(b);*/
                          // if((position+1)==stylist_list.size()) {loaded = true;}
                      // }

                   // }
                //}
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
                    //TicketScreenActivity.stylist_position = (Integer) rb.getTag();
                    rb.setChecked(true);
                    notifyDataSetChanged();

                }
            });*/
    }


}
package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.Customer;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.MyIntent;
import app.reservation.acbasoftare.com.reservation.App_Objects.ReservationTicket;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;


public class ReservationActivity extends AppCompatActivity {
    public static boolean isSuccessful;//thought for signaling whether payment was succesful& reserved
    private FirebaseStore store;
    private Stylist stylist;
    private SalonService salon_service;
    private HashMap<Date, Date> reserved_appointments;//global reservations for all dates
    private Date today;
    private Spinner s;
    private Customer customer;
    private EditText comments;
    private ReservationTicket reservationTicket;
    private ArrayList<String> list_formatted;
    private int selectedPosition;

    public FirebaseStore getStore(){
        return store;
    }
    public Date getReserveDate(){
        return today;
    }
    public TimeSet getTimeSet(){
        return new TimeSet(today,s.getSelectedItem().toString());
    }
    public SalonService getSalonService(){
        return this.salon_service;
    }
    public Stylist getStylist(){
        return this.stylist;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        this.reservationTicket = new ReservationTicket();
        customer=new Customer(this);
        MyIntent myIntent = this.getIntent().getParcelableExtra("myIntent");
        today =   myIntent.date; // MainActivity.myIntent.date;
        isSuccessful = false;
        //store =  myIntent.store;
        stylist = myIntent.stylist;
        salon_service = myIntent.salonService;
        selectedPosition = myIntent.selectedPosition;
        setUpGUI();
       // reserved_appointments = store.getReservation().getAppointments(stylist);//"Store: " + store + " Stylist: " + stylist + " ss: " + salon_service
        new Thread(new Runnable() {
            @Override
            public void run() {
                populateAvailableTimes();
            }
        }).start();
        Button back = (Button) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });
        Button reserv = (Button) findViewById(R.id.button_reserve_appointment);
        reserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserve();
                //reserve
            }
        });
    }

    private void setUpGUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        TextView avail = (TextView) findViewById(R.id.heading_tab3_textview);
        avail.setText(avail.getText() + sdf.format(today));
        TextView tv_sty = (TextView) findViewById(R.id.textView_stylist_tab3);
        tv_sty.setText("Stylist: " + stylist.getName().toUpperCase());
        TextView tv_service = (TextView) findViewById(R.id.textView_service_resactivity);
        tv_service.setText("Service: " + salon_service.getName().toUpperCase() + "\nPrice: " + salon_service.getFormattedPrice() + "\nDuration: " + salon_service.getDuration());
         s = (Spinner) findViewById(R.id.spinner_times);
        comments = (EditText) findViewById(R.id.EditText_comment);


    }
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected", selectedPosition);

    }
public String getCommentSection(){
    return this.comments.getText().toString();
}
    private void reserve() {
        CreditCardDialog ccd = new CreditCardDialog(this,store,stylist,salon_service, new TimeSet(today,s.getSelectedItem().toString()));
        ccd.showCreditCardDialogForReservationCalendar();

    }
    public Customer getCustomer(){
        return this.customer;
    }

    /**
     * Will populate the available times based on the variable SalonService-duration
     * 1) populates a List that only has Todays Time taken...
     * 2) Loop through OpenTime->ClosingTime checking and adding range of times only thats not in taken list
     */
    private void populateAvailableTimes() {
        try {
            ArrayList<Date> keys = new ArrayList<>(this.reserved_appointments.keySet());
            Collections.sort(keys);
            ArrayList<ArrayList<Date>> taken_list = new ArrayList<>();///keep record of the kept times in 5:00pm-6:00pm format
            for (Date start : keys) { //i think this is a repeatt of Util function
                //getTodaysTime...
                if(Utils.isDayOrMoreAfter(start,today)) break;
                if (!Utils.isSameDay(today, start)) continue;//skipp if not same day
                Date end = reserved_appointments.get(start);
                ArrayList<Date> dl = new ArrayList<>();
                dl.add(start);
                dl.add(end);
                taken_list.add(dl);
            }//end of loop have all todays reservations
            //gett abs_lower and upper bounds
            Date open_hour = Utils.getDateFromAGivenTime(today, store.getOpen_time());//in format of date time
            Date closing_hour = Utils.getDateFromAGivenTime(today, store.getClose_time());//end bounds
            list_formatted = Utils.getAvailableTimes(open_hour, closing_hour, taken_list, salon_service.getDuration().getMilliSeconds());

            ArrayAdapter<String> adpt = new ArrayAdapter<String>(this.getApplicationContext(), R.layout.spinner_layout_time, list_formatted);
            s.setAdapter(adpt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
       // MainActivity.myIntent = null;//reset to gc
        finish();
    }

    public void createReservationTicket() {
        this.reservationTicket = new ReservationTicket(stylist,salon_service,customer,getTimeSet());
        removeCurrentTimeChoice();
        //goBack();
    }

    public void removeCurrentTimeChoice() {
        String selected = s.getSelectedItem().toString();
        TimeSet ts = new TimeSet(today,selected);

      //  Store s = ma.store_list.get(selectedPosition);//get store
        //Log.d("BEFORE:: ",(s.getReservations().getAppointments(stylist).get(ts.getLowerBound())==null)+"");

        //s.getReservation().removeReservationDate(stylist,ts.getLowerBound());
        //ma.store_list.remove(selectedPosition);
        //ma.store_list.add(selectedPosition,s);
       // Log.d("AFTER:: ",(s.getReservations().getAppointments(stylist).get(ts.getLowerBound())==null)+"");

        this.list_formatted.remove(this.s.getSelectedItem());
        ((ArrayAdapter<String>)this.s.getAdapter()).notifyDataSetChanged();
    }
}

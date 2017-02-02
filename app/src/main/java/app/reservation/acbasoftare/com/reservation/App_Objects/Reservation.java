package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.a;

/**
 * Created by Gabe-ACBA on 12/1/2016.
 * This class will hold all of the STORES - Stylists reservations
 * Idea is to hold a hashmap of DateTimes(mysql) to fill a CalendarVeiw
 */
public class Reservation implements Parcelable {
    private HashMap<TimeSet, ReservationDetails> time_range_details_hm;
    private HashMap<Stylist, HashMap<Date, Date>> reservation_hm;//alias for stylist appointments
    private HashMap<String, HashMap<TimeSet,ReservationDetails>> days_reserved_hm;//String is represented as MMM/dd/yyyy. The reason i used String
    //and not Date is because two dates that are equal dont have the same hashCode() however two strings that are eqaul do.....


    public Reservation() {
        this.reservation_hm=new HashMap<>();
        this.days_reserved_hm = new HashMap<>();//added for testing got error here
        this.time_range_details_hm = new HashMap<>();//added for testing got an err for^^^ delete both if dont work
    }

    public Reservation(ArrayList<Stylist> list) {
        populateMap(list);
    }

    /**
     * For the use of storing one stylist all of the appointments
     * aka EmployeeActivity
     *
     * @param s
     */
    public Reservation(Stylist s) {
        this.reservation_hm=new HashMap<>();
        this.reservation_hm.put(s, new HashMap<Date, Date>());
        this.time_range_details_hm=new HashMap<>();//store the starttime as key and get Details for that time
        this.days_reserved_hm=new HashMap<>();
    }


    protected Reservation(Parcel in) {
        Bundle b = in.readBundle();
        this.days_reserved_hm = (HashMap<String, HashMap<TimeSet, ReservationDetails>>) b.getSerializable("days");
        this.time_range_details_hm = (HashMap<TimeSet, ReservationDetails>) b.getSerializable("time");
        this.reservation_hm = (HashMap<Stylist, HashMap<Date, Date>>) b.getSerializable("res");
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public void populateMap(ArrayList<Stylist> list) {
        this.reservation_hm=new HashMap<>();
        for(Stylist s : list) {
            if(!s.getId().equalsIgnoreCase("-1"))
                this.reservation_hm.put(s, new HashMap<Date, Date>());
        }
    }

    public void setDateTime(Stylist s, Date start, Date end) {
        HashMap<Date, Date> obj=this.reservation_hm.get(s);
        obj.put(start, end);
        this.reservation_hm.put(s, obj);
    }

    //Means no stylists or keys were inputed yet
    public boolean isEmpty() {
        return this.reservation_hm == null || this.reservation_hm.size() == 0;
    }

    public void setDateTime(Stylist s, String formatted_start_datetime, String formatted_end_datetime) {
        Date start=convertStringToDate(formatted_start_datetime);
        Date end=convertStringToDate(formatted_end_datetime);
        HashMap<Date, Date> obj=this.reservation_hm.get(s);
        //  if (!obj.containsKey(start)) { //start date is not set
        obj.put(start, end);//set the DateTime appointment
        // }
        this.reservation_hm.put(s, obj);
    }

    public void setDateTime(Stylist s, String formatted_start_datetime, String formatted_end_datetime, ReservationDetails rd) {
        Date start=convertStringToDate(formatted_start_datetime);
        Date end=convertStringToDate(formatted_end_datetime);
        HashMap<Date, Date> obj=this.reservation_hm.get(s);
        obj.put(start, end);//set the DateTime appointment
        TimeSet ts = new  TimeSet(start,end);
        this.reservation_hm.put(s, obj);
        this.time_range_details_hm.put(ts, rd);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
      //  Date date = Utils.convertToWholeDay(start);
        String date = sdf.format(start);
        if(!this.days_reserved_hm.containsKey(date)){
            this.days_reserved_hm.put(date,new HashMap<TimeSet, ReservationDetails>());
        }
        HashMap<TimeSet,ReservationDetails> d = this.days_reserved_hm.get(date);
        d.put(ts,rd);
        this.days_reserved_hm.put(date,d);
    }

    /**
     * Get all the Dates the stylist as reserved
     * @return
     */
    public ArrayList<String> getDaysReserved(){
        ArrayList<String> list =  new ArrayList<>(this.days_reserved_hm.keySet());
        Collections.sort(list);
        return list;
    }

    /**
     * Gets details about a certain DAy and thAT particular timeset
     * @param day
     * @param ts
     * @return
     */
    public ReservationDetails getReservationDetails(Date day,TimeSet ts){
        return this.days_reserved_hm.get(day).get(ts);
    }

    /**
     * Is a time range returns the details for that timerange
     * Is different from Date day, Timeset that method gets a timeset from a Date(Day) not TimeSet(DateTime)
     * @param ts
     * @return
     */
    public ReservationDetails getReservationDetails(TimeSet ts) {
        return this.time_range_details_hm.get(ts);
    }

    /**
     * Helper method for addDateTime
     * in 24 hour format
     *
     * @param fs
     * @return
     */
    private Date convertStringToDate(String fs) {
        Date date=null;
        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date=sdf.parse(fs);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public String toString() {
        String t="";
        for(Stylist s : this.reservation_hm.keySet()) {
            t+="Sty: " + s.getName() + " Res: " + this.reservation_hm.get(s).size();
        }
        return t;
    }

    /**
     * */
    public HashMap<Stylist, HashMap<Date, Date>> getReservationHashMap() {
        return this.reservation_hm;
    }

    void setReservationHashMap(HashMap<Stylist, HashMap<Date, Date>> hm) {
        this.reservation_hm=hm;
    }

    public HashMap<Date, Date> getAppointments(Stylist s) {
        return this.reservation_hm.get(s);
    }
    public void removeReservationDate(Stylist s, Date start){
        HashMap<Date,Date> d = this.reservation_hm.get(s);
        d.remove(start);
        this.reservation_hm.put(s,d);
    }


    public HashMap<TimeSet,ReservationDetails> getReservationDetailsHashMap() {return this.time_range_details_hm;}

    public ArrayList<Date> getDaysReservedList(Stylist stylist) {
        ArrayList<Date> rl = new ArrayList<Date>(this.reservation_hm.get(stylist).keySet());
        Collections.sort(rl);
        ArrayList<Date> days = new ArrayList<>();
        Date day=rl.get(0);
        days.add(day);
        for(int i=1; i<rl.size(); ++i){
            Date next=rl.get(i);
                if(!Utils.isSameDay(day,next)){
                    day=next;
                    days.add(next);
                }
        }
        return days;
    }

    /**
     * This method is responsible for producing the arraylist of ALL reservations for the specified day. In the format
     * of timeset..
     * @param day
     * @param s
     * @return
     */
    public ArrayList<TimeSet> getTodaysTimes(Date day,Stylist s){
        return Utils.getTodaysTimeSetList(day,this.getAppointments(s));
    }
    public ArrayList<TimeSet> getDayTimes(Date day){
        ArrayList<TimeSet> list=null;
        try {


            SimpleDateFormat sdf=new SimpleDateFormat("MMM/dd/yyyy");
        list =new ArrayList<TimeSet>(this.days_reserved_hm.get(sdf.format(day)).keySet());
            Collections.sort(list);
        }catch(Exception e){
            return  new ArrayList<>();
        }
            return list;
    }

//MEANT GETTERS FOR FIREBASE DONT ACTUALLY USE
    public HashMap<TimeSet, ReservationDetails> getTime_range_details_hm() {
        return time_range_details_hm;
    }

    public HashMap<Stylist, HashMap<Date, Date>> getReservation_hm() {
        return reservation_hm;
    }

    public HashMap<String, HashMap<TimeSet, ReservationDetails>> getDays_reserved_hm() {
        return days_reserved_hm;
    }//END O FIREBASE GETTERS

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       Bundle b = new Bundle();
        b.putSerializable("days",this.days_reserved_hm);
        b.putSerializable("time",this.time_range_details_hm);
        b.putSerializable("res",this.reservation_hm);
        parcel.writeBundle(b);
    }
}
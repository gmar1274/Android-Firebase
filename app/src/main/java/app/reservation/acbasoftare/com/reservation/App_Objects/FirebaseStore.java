package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;
<<<<<<< HEAD

import java.text.SimpleDateFormat;
=======
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
import java.util.Date;

/**
 * Created by user on 2/3/17.
 */

public class FirebaseStore implements Parcelable , Comparable<FirebaseStore>{
    private String name, address, citystate, phone, open_time, close_time;
    private String email, password,card_id,  subscription_id,google_place_id;
    private LatLng location;
    private double ticket_price, reservation_calendar_price;//changed from big decimal to double
    private long store_number;
    private double miles_away;
    private long current_ticket;

    public long getCurrent_ticket() {
        return current_ticket;
    }

    public void setCurrent_ticket(long current_ticket) {
        this.current_ticket = current_ticket;
    }

    public double getMiles_away() {
        return miles_away;
    }

    public void setMiles_away(double miles_away) {
        this.miles_away = miles_away;
    }

    public FirebaseStore() {
    }
    public FirebaseStore(long sn, String name, String addr, String citystate, String phone, LatLng loc, double ticket_price, String open, String close, double cprice, String email, String password, String card_id, String subscription_id, String google) {
        this.store_number=sn;
        this.name=name;
        this.address=addr;
        this.citystate=citystate;
        this.phone=phone;
        this.location = loc;
        this.ticket_price =ticket_price;
        this.open_time=open;
        this.close_time=close;
        this.reservation_calendar_price = cprice;
        this.email=email;
        this.password=password;
        this.card_id=card_id;
        this.subscription_id=subscription_id;
        this.google_place_id = google;
    }

    protected FirebaseStore(Parcel in) {
        name = in.readString();
        address = in.readString();
        citystate = in.readString();
        phone = in.readString();
        open_time = in.readString();
        close_time = in.readString();
        email = in.readString();
        password = in.readString();
        card_id = in.readString();
        subscription_id = in.readString();
        google_place_id = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        ticket_price = in.readDouble();
        reservation_calendar_price = in.readDouble();
        store_number = in.readLong();
    }

    public static final Creator<FirebaseStore> CREATOR = new Creator<FirebaseStore>() {
        @Override
        public FirebaseStore createFromParcel(Parcel in) {
            return new FirebaseStore(in);
        }

        @Override
        public FirebaseStore[] newArray(int size) {
            return new FirebaseStore[size];
        }
    };

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public void setGoogle_place_id(String google_place_id) {
        this.google_place_id = google_place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCitystate() {
        return citystate;
    }

    public void setCitystate(String citystate) {
        this.citystate = citystate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public double getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(double ticket_price) {
        this.ticket_price = ticket_price;
    }

    public double getReservation_calendar_price() {
        return reservation_calendar_price;
    }

    public void setReservation_calendar_price(double reservation_calendar_price) {
        this.reservation_calendar_price = reservation_calendar_price;
    }

    public long getStore_number() {
        return store_number;
    }

    public void setStore_number(long store_number) {
        this.store_number = store_number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(citystate);
        parcel.writeString(phone);
        parcel.writeString(open_time);
        parcel.writeString(close_time);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(card_id);
        parcel.writeString(subscription_id);
        parcel.writeString(google_place_id);
        parcel.writeParcelable(location, i);
        parcel.writeDouble(ticket_price);
        parcel.writeDouble(reservation_calendar_price);
        parcel.writeLong(store_number);
    }
<<<<<<< HEAD
public String toString(){
    return this.name+" "+miles_away;
}
=======

>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
    @Override
    public int compareTo(FirebaseStore firebaseStore) {
        if(this.miles_away < firebaseStore.miles_away){
            return -1;
        }else if(this.miles_away> firebaseStore.miles_away){
            return 1;
        }else return 0;
    }
    public String formatHoursTo12hours(){
        SimpleDateFormat h = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        try{
            Date o = h.parse(this.open_time);
            Date c = h.parse(this.close_time);
            return sdf.format(o) +" - "+sdf.format(c);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "N/A";
    }

    public String displayIsAvailable() {
        SimpleDateFormat h = new SimpleDateFormat("HH:mm:ss");
        boolean open = false;
        try{


            Date o = h.parse(this.open_time);
            Date c = h.parse(this.close_time);

            String now_time = h.format(new Date());
            Date now = h.parse(now_time);

            //Log.e("times::::::","o: "+o+" now: "+now+" c: "+c);
            open =   now.compareTo(o) >= 0 && now.compareTo(c) <= 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        if(open){
            return "OPEN TODAY";
        }else{
            return "CLOSED TODAY";
        }
    }
}

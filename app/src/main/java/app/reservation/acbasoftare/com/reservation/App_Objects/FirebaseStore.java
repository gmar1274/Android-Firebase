package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2/3/17.
 */

public class FirebaseStore implements  Comparable<FirebaseStore>, Parcelable{
    private String name, address, phone;
    private String email, password,card_id,  subscription_id,google_place_id;
    private LatLng location;
    private double ticket_price, reservation_calendar_price;//changed from big decimal to double
    private long store_number;
    private double miles_away;
    private long current_ticket;
    private List<String> period; //google's naming of key: DAY - VALUE: X:XX AM/PM - Y:YY AM/PM

    public FirebaseStore(String google_id){
        this.google_place_id = google_id;
    }
    protected FirebaseStore(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        email = in.readString();
        password = in.readString();
        card_id = in.readString();
        subscription_id = in.readString();
        google_place_id = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        ticket_price = in.readDouble();
        reservation_calendar_price = in.readDouble();
        store_number = in.readLong();
        miles_away = in.readDouble();
        current_ticket = in.readLong();
        period = in.createStringArrayList();
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

   /* public HashMap<String, String> getPeriod() {
        if(period == null){
            period = new HashMap<>();
        }
        return period;
    }*/
    //public void setPeriod(HashMap<String, String> period) {this.period = period;}

    public FirebaseStore() {
    }
    public FirebaseStore(Place p , long store_number, String email,String password , Map<String,String> period){
        this.name = p.getName().toString();
        this.address = p.getAddress().toString();
        this.phone = p.getPhoneNumber().toString().replace(" ","").replace("+","").replace("-",""); ///strip +1 909-123-4567 to 19091234567
        this.current_ticket = 1;
        this.email = email;
       // this.citystate = p.getAddress().toString();
        this.google_place_id= p.getId();
        this.location = new LatLng(p.getLatLng().latitude,p.getLatLng().longitude);
        this.store_number = store_number;
        this.password = Encryption.encryptPassword(password);
       this.period = new ArrayList<>(period.values());
    }

    public FirebaseStore(long sn, String name, String addr, String citystate, String phone, LatLng loc, double ticket_price, String open, String close, double cprice, String email, String password, String card_id, String subscription_id, String google) {
        this.store_number=sn;
        this.name=name;
        this.address=addr;
       // this.citystate=citystate;
        this.phone=phone;
        this.location = loc;
        this.ticket_price =ticket_price;
       // this.open_time=open;
        //this.close_time=close;
        this.reservation_calendar_price = cprice;
        this.email=email;
        this.password=password;
        this.card_id=card_id;
        this.subscription_id=subscription_id;
        this.google_place_id = google;
    }

 /*int size = in.readInt();
        for(int i = 0 ; i < size; ++i){
            String key = in.readString();
            String val = in.readString();
            period.put(key,val);
        }*/



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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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



    /* parcel.writeInt(period.size());
            for(Map.Entry<String,String> entry : period.entrySet()){
                parcel.writeString(entry.getKey());
                parcel.writeString(entry.getValue());
            }*/
    @Override
    public int compareTo(FirebaseStore firebaseStore) {
        if(this.miles_away < firebaseStore.miles_away){
            return -1;
        }else if(this.miles_away> firebaseStore.miles_away){
            return 1;
        }else return 0;
    }
    public String formatHoursTo12hours(){
        if(period!=null && period.size()>0){
            return period.get(Calendar.getInstance().DAY_OF_WEEK);
            //Calendar cal = Calendar.getInstance();
            //String[] time = period.get(cal.DAY_OF_WEEK).split("-");
            //return
        }
        return "N/A";
        /*SimpleDateFormat h = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        try{
            Date o = h.parse(this.open_time);
            Date c = h.parse(this.close_time);
            return sdf.format(o) +" - "+sdf.format(c);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "N/A";*/
    }

    public String displayIsAvailable() {
        SimpleDateFormat h = new SimpleDateFormat("HH:mm:ss");
        boolean open = false;
        try{
            //Date o = h.parse(this.open_time);
            //Date c = h.parse(this.close_time);

           // String now_time = h.format(new Date());
            //Date now = h.parse(now_time);
            Calendar cal = Calendar.getInstance();
            String[] time = period.get(cal.DAY_OF_WEEK).split("-");//
            SimpleDateFormat ampm = new SimpleDateFormat("hh:mm a");
            Date now =  ampm.parse(ampm.format(new Date()));
            Date o =  ampm.parse(time[0]);
            Date c= ampm.parse(time[1]);

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
    @Override
    public int hashCode(){
        return this.name.hashCode() + String.valueOf(this.store_number).hashCode();
    }
    @Override
    public  boolean equals(Object o){
        FirebaseStore s = (FirebaseStore) o;
        return this.google_place_id.equals(s.google_place_id); //this.name.equalsIgnoreCase(s.name) && this.store_number==s.store_number && this.google_place_id.equalsIgnoreCase(s.google_place_id);
    }
    public String toString(){
        return "Store ID: "+store_number+" peroid: "+period;
    }

    /*public FirebaseStore(FirebaseStore s , Map<String,String> map){
        this.address = s.address;
        this.name = s.name;
        this.google_place_id = s.google_place_id;
        this.phone = s.phone;
        this.store_number = s.store_number;
        this.email = s.email;
        this.password = s.password;
        this.card_id = s.card_id;
        this.subscription_id = s.subscription_id;
        this.location = s.location;
        this.ticket_price = s.ticket_price ;
        this.reservation_calendar_price = 1;
        this.current_ticket = s.current_ticket;
        this.period = map;
    }*/

    public List<String> getPeriod() {
        return period;
    }

    public void setPeriod(List<String> period) {
        this.period = period;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
      parcel.writeString(phone);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(card_id);
        parcel.writeString(subscription_id);
        parcel.writeString(google_place_id);
        parcel.writeParcelable(location, i);
        parcel.writeDouble(ticket_price);
        parcel.writeDouble(reservation_calendar_price);
        parcel.writeLong(store_number);
        parcel.writeDouble(miles_away);
        parcel.writeLong(current_ticket);
        parcel.writeStringList(period);
    }
}

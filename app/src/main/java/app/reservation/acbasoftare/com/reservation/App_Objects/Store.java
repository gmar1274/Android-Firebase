package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 2016-08-05.
 */
public class Store implements Parcelable {
    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel source) {
            return new Store(source);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };
    private String name, address, citystate, phone, open_time, close_time;
    private String email, password,card_id,  subscription_id;
    private  long current_ticket;
    //private long id;
    private int pos;
    private LatLng location;
    private double miles_away;
    private double ticket_price, reservation_calendar_price;//changed from big decimal to double
    private HashMap<Integer, SalonService> services = null;
    private HashMap<String, Stylist> stylistHashMap = null;///this should hold all the stylists info
    private Reservation reservation;
    private long store_number;

    /**DEBUG CONSTRUCTOR
     *
     * */
    public Store(){
        this.phone = "9091234567";
        this.ticket_price = 1.99;//new BigDecimal(1.99);
        this.name = "TEST STORE";
    }

    public Store(String name) {
        this.name = name;
        this.services = new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
    }

    public Store(String name, String address, String citystate, String phone, double lat, double lon, int pos, double milesaway, double ticket_price, String open, String close, double cprice) {
        this.name = name;
        this.open_time = open;
        this.close_time = close;
        this.address = address;
        this.citystate = citystate;
        //this.id=id;
        this.phone = phone;
        this.location = new LatLng(lat, lon);
        this.pos = pos;
        this.miles_away = milesaway;
        this.ticket_price = ticket_price;
        this.services = new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
        this.reservation_calendar_price = cprice;//changed the big decimal to double
    }
    public Store(long storeNumber,String name, String address, String citystate, String phone, double lat, double lon, int pos, double milesaway, double ticket_price, String open, String close, double cprice) {
        this.name = name;
        this.open_time = open;
        this.close_time = close;
        this.address = address;
        this.citystate = citystate;
        //this.id=id;
        this.phone = phone;
        this.location = new LatLng(lat, lon);
        this.pos = pos;
        this.miles_away = milesaway;
        this.ticket_price = ticket_price;
        this.services = new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
        this.reservation_calendar_price = cprice;//changed the big decimal to double
        this.store_number=storeNumber;
    }
    protected Store(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.citystate = in.readString();
        this.phone = in.readString();
        this.pos = in.readInt();
        this.location = in.readParcelable(LatLng.class.getClassLoader());
        this.miles_away = in.readDouble();
        this.ticket_price =  (Double)in.readSerializable();//(BigDecimal) in.readSerializable();
        this.reservation_calendar_price = (Double) in.readSerializable();
        in.readMap(services, SalonService.class.getClassLoader());
        in.readMap(stylistHashMap, Stylist.class.getClassLoader());
        this.reservation = in.readParcelable(Reservation.class.getClassLoader());
    }

    public Store(long storeNumber, String name, String addr, String citystate, String phone, double lat, double lon, int i, double miles_away, double ticket_price, String open, String close, double cprice, String email, String password, long current_ticket, String card_id, String subscription_id) {
        this.name = name;
        this.open_time = open;
        this.close_time = close;
        this.address = address;
        this.citystate = citystate;
        //this.id=id;
        this.phone = phone;
        this.location = new LatLng(lat, lon);
        this.pos = pos;
       // this.miles_away = milesaway;
        this.ticket_price = ticket_price;
        this.services = new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
        this.reservation_calendar_price = cprice;//changed the big decimal to double
        this.store_number=storeNumber;
        this.current_ticket=current_ticket;
        this.email=email;
        this.password=password;
        this.card_id=card_id;
        this.subscription_id=subscription_id;




   }

    public String getOpen_time() {
        return open_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCard_id() {
        return card_id;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public long getCurrent_ticket() {
        return current_ticket;
    }



    public double getTicket_price() {
        return ticket_price;
    }

    public double getReservation_calendar_price() {
        return reservation_calendar_price;
    }

    public HashMap<Integer, SalonService> getServices() {
        return services;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public long getStore_number() {
        return store_number;
    }

    public String getOpeningHour() {
        return this.open_time;
    }

    public String getClosingHour() {
        return this.close_time;
    }

    public double getReservationPrice() {
        return this.reservation_calendar_price;
    }

    public String getID() {
        return this.phone;
    }

    public Reservation getReservations() {
        return this.reservation;
    }

    public ArrayList<Stylist> getStylistArrayList() {
        return new ArrayList<Stylist>(this.stylistHashMap.values());
    }

    public ArrayList<SalonService> getSalonServicesArrayList() {
        return new ArrayList<SalonService>(this.services.values());
    }

    public void initializeReservations() {
        this.reservation = new Reservation(this.getStylistArrayList());
    }

    public HashMap<String, Stylist> getStylistHashMap() {
        return this.stylistHashMap;
    }

    public void setStylistHashMap(HashMap<String, Stylist> hm) {
        this.stylistHashMap = hm;
    }

    public boolean isSalonServicesLoaded() {
        return this.services != null && this.services.size() > 0;
    }//if theres n or more items greater than 0 and not null then loaded

    public HashMap<Integer, SalonService> getSalonServices() {
        return this.services;
    }

    public void addService(SalonService ss) {
        if (this.services == null) this.services = new HashMap<>();
        this.services.put(ss.getId(), ss);
    }

    public double getTicketPrice() {
        return (this.ticket_price);
    }

    public double getMilesAway() {
        return this.miles_away;
    }

    public int getPos() {
        return this.pos;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCitystate() {
        return this.citystate;
    }

    public String toString() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.citystate);
        dest.writeString(this.phone);
        dest.writeInt(this.pos);
        dest.writeParcelable(this.location, flags);
        dest.writeDouble(this.miles_away);
        dest.writeDouble(this.ticket_price);
        dest.writeMap(this.services);
        dest.writeMap(stylistHashMap);
        dest.writeParcelable(this.reservation, flags);
        dest.writeDouble(this.reservation_calendar_price);
    }
}

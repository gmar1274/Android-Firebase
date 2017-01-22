package app.reservation.acbasoftare.com.reservation.App_Objects;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private LatLng location;
    private double miles_away;
    private double ticket_price, reservation_calendar_price;//changed from big decimal to double
    private HashMap<Integer, SalonService> services = null;
    private HashMap<String, Stylist> stylistHashMap = null;///this should hold all the stylists info
    private Reservation reservation;
    private long store_number;
    private List<Stylist> stylistList;

    /**DEBUG CONSTRUCTOR
     *
     * */
    public Store(){
        this.phone = "9091234567";
        this.ticket_price = 1.99;//new BigDecimal(1.99);
        this.name = "TEST STORE";
        this.services=new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
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
        this.store_number = pos;
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
        //this.pos = pos;
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
       // this.pos = in.readInt();
        this.location = in.readParcelable(LatLng.class.getClassLoader());
        this.miles_away = in.readDouble();
        this.ticket_price =  in.readDouble();//(BigDecimal) in.readSerializable();
        this.reservation_calendar_price = in.readDouble();
        in.readMap(services, SalonService.class.getClassLoader());
        in.readMap(stylistHashMap, Stylist.class.getClassLoader());
        this.reservation = in.readParcelable(Reservation.class.getClassLoader());
    }

    public Store(long storeNumber, String name, String addr, String citystate, String phone, double lat, double lon, int i, double miles_away, double ticket_price, String open, String close, double cprice, String email, String password, long current_ticket, String card_id, String subscription_id) {
        this.name = name;
        this.open_time = open;
        this.close_time = close;
        this.address = addr;
        this.citystate = citystate;
        //this.id=id;
        this.phone = phone;
        this.location = new LatLng(lat, lon);
       // this.pos = pos;
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
        this.services = new HashMap<>();



   }



    public HashMap<Integer, SalonService> getServices() {
        return services;
    }
    public Reservation getReservations() {
        return this.reservation;
    }

    public ArrayList<Stylist> getStylistList() {
        return new ArrayList<Stylist>(this.stylistHashMap.values());
    }
    public ArrayList<SalonService> getSalonServicesArrayList() {
        return new ArrayList<SalonService>(this.services.values());
    }

    public void initializeReservations() {
        this.reservation = new Reservation(this.getStylistList());
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


    public LatLng getLocation() {
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public void setServices(HashMap<Integer, SalonService> services) {
        this.services = services;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.citystate);
        dest.writeString(this.phone);
        //dest.writeInt(this.pos);
        dest.writeParcelable(this.location, flags);
        dest.writeDouble(this.miles_away);
        dest.writeDouble(this.ticket_price);
        dest.writeMap(this.services);
        dest.writeMap(stylistHashMap);
        dest.writeParcelable(this.reservation, flags);
        dest.writeDouble(this.reservation_calendar_price);

    }

    public void updateStylistWait(String id) {
       Stylist s = this.stylistHashMap.get(id);
        s.incrementWait();
        this.stylistHashMap.put(id,s);
    }
    public void setStylistList(List<Stylist> l){
        if(this.stylistList == null){
            this.stylistList = new ArrayList<>();
        }
        this.stylistList = l;
    }
    public List<Stylist> getStylistListFirebaseFormat(){return this.stylistList;}


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCitystate() {
        return citystate;
    }

    public String getPhone() {
        return phone;
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

    public double getMiles_away() {
        return miles_away;
    }

    public double getTicket_price() {
        return ticket_price;
    }
    public double getReservation_calendar_price() {
        return reservation_calendar_price;
    }
    public long getStore_number() {
        return store_number;
    }
}

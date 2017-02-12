package app.reservation.acbasoftare.com.reservation.App_Objects;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by user on 2016-08-05.
 */
public class Store implements Parcelable, Comparable<Store>  {

    private String name, address, citystate, phone, open_time, close_time;
    private String email, password,card_id,  subscription_id;

    private LatLng location;
    private double miles_away;
    private double ticket_price, reservation_calendar_price;//changed from big decimal to double
    private HashMap<Integer, SalonService> services = null;
    private HashMap<String, Stylist> stylistHashMap = null;///this should hold all the stylists info
    private Reservation reservation;
    private long store_number;
    private List<Ticket> tickets;
    private long current_ticket;
    /**DEBUG CONSTRUCTOR
     *
     * */
    public Store(){
        this.current_ticket = 0;
        this.phone = "9091234567";
        this.ticket_price = 1.99;//new BigDecimal(1.99);
        this.name = "TEST STORE";
        this.services=new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
        this.tickets = new ArrayList<>();
    }

    public Store(String name) {
        this.name = name;
        this.services = new HashMap<>();
        this.stylistHashMap = new HashMap<>();
        this.reservation = new Reservation();
        this.tickets = new ArrayList<>();
        this.current_ticket = 0;
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
        this.tickets = new ArrayList<>();
        this.current_ticket  = 0;
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
        this.tickets = new ArrayList<>();
        this.current_ticket = 0;
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
        this.tickets = new ArrayList<>();


   }

    protected Store(Parcel in) {
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
        location = in.readParcelable(LatLng.class.getClassLoader());
        miles_away = in.readDouble();
        ticket_price = in.readDouble();
        reservation_calendar_price = in.readDouble();
        reservation = in.readParcelable(Reservation.class.getClassLoader());
        store_number = in.readLong();
        tickets = in.createTypedArrayList(Ticket.CREATOR);
        current_ticket = in.readLong();
    }

    public static final Creator<Store> CREATOR = new Creator<Store>() {
        @Override
        public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
        public Store[] newArray(int size) {
            return new Store[size];
        }
    };

    public HashMap<Integer, SalonService> getServices() {
        return services;
    }
    public Reservation getReservation() {
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

    public List<Ticket> getTickets() {
        return tickets;
    }
    public void addTicket(Ticket t){this.tickets.add(t);}
    public void deleteTicket(Ticket t){this.tickets.remove(t);}
    public void deleteTicket(int pos){this.tickets.remove(pos);}
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
    public void addStoreStylist(){
        if(this.stylistHashMap == null){
            stylistHashMap = new HashMap<>();
        }
        Stylist store = new Stylist();
        store.setStore_id(this.phone);
        store.setPhone(this.phone);
        stylistHashMap.put(store.getId(),store);
    }
    public void addService(SalonService ss) {
        if (this.services == null) this.services = new HashMap<>();
        this.services.put(ss.getId(), ss);
    }


    public LatLng getLocation() {
        return location;
    }


    public void setServices(HashMap<Integer, SalonService> services) {
        this.services = services;
    }



    public void updateStylistWait(String id) {
       Stylist s = this.stylistHashMap.get(id);
        s.incrementWait();
        this.stylistHashMap.put(id,s);
    }

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
    public void incrementCurrentTicket(){
        this.current_ticket += 1;
    }

    public void setCurrentStoreTicket(long unique_id) {
        this.current_ticket=unique_id;
    }

    public void updateStylist(Stylist sty) {
        if (this.stylistHashMap == null) {
            this.stylistHashMap = new HashMap<>();
        }
        this.stylistHashMap.put(sty.getId(), sty);
    }
public String toString(){return this.getName();}
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
        parcel.writeParcelable(location, i);
        parcel.writeDouble(miles_away);
        parcel.writeDouble(ticket_price);
        parcel.writeDouble(reservation_calendar_price);
        parcel.writeParcelable(reservation, i);
        parcel.writeLong(store_number);
        parcel.writeTypedList(tickets);
        parcel.writeLong(current_ticket);
    }
    public void setDistanceAway(double miles){
        this.miles_away = miles;
    }

    @Override
    public int compareTo(Store store) {
        if(store.getMiles_away() < this.getMiles_away())return 1;
        else if(store.getMiles_away()> this.getMiles_away())return -1;
        return 0;
    }
    public void determineCurrentTicketAfterFirebaseLoads(){
        if(tickets==null || tickets.size()==0)return;
        this.current_ticket = this.tickets.get(0).unique_id;
    }
}

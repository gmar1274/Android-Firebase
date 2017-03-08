package app.reservation.acbasoftare.com.reservation.App_Objects;

import com.google.android.gms.location.places.Place;

import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 2/3/17.
 */

public class FirebaseStoreMetaData implements Comparable {

    private long store_number;
    private String phone,google_place_id;
    private LatLng location;
    public FirebaseStoreMetaData(){

    }

    /**
     * STORE REGISTRATION
     * @param store
     * @param place
     */
    public FirebaseStoreMetaData(FirebaseStore store, Place place) {
        this.store_number = store.getStore_number();
        this.phone = Utils.formatPhoneNumber(place.getPhoneNumber().toString());
        this.google_place_id = place.getId();
        LatLng loc = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
        this.location = loc;
    }

    public String getGoogle_place_id() {
        return google_place_id;
    }

    public void setGoogle_place_id(String google_place_id) {
        this.google_place_id = google_place_id;
    }

    public  FirebaseStoreMetaData(long store_number, String phone, LatLng location, String goole){
    this.google_place_id = goole;
    this.store_number = store_number;
    this.phone = phone;
    this.location = location;
}
    public void setLocation(LatLng location) {
        this.location = location;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStore_number(long store_number) {
        this.store_number = store_number;
    }

    public long getStore_number() {
        return store_number;
    }

    public String getPhone() {
        return phone;
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    public int compareTo(Object o) {
        FirebaseStoreMetaData m = (FirebaseStoreMetaData) o;
        if(this.store_number<m.store_number)return -1;
        else if(this.store_number>m.store_number)return 1;
        return 0;
    }
    @Override
    public boolean equals(Object o){
        FirebaseStoreMetaData m = (FirebaseStoreMetaData) o;
        return this.google_place_id.equals(m.getGoogle_place_id());
    }
    @Override
    public int hashCode(){
        return this.google_place_id.hashCode();
    }



}

package app.reservation.acbasoftare.com.reservation.App_Objects;

/**
 * Created by user on 2/3/17.
 */

public class FirebaseStoreMetaData {

    private long store_number;
    private String phone,google_place_id;
    private LatLng location;
    public FirebaseStoreMetaData(){

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
}

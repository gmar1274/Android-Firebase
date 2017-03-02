package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2017-02-11.
 */
public class FirebaseEmployee implements Parcelable {

    private String name,id, app_password, app_username, store_number, store_phone;
    public FirebaseEmployee(){

    }

    protected FirebaseEmployee(Parcel in) {
        name = in.readString();
        id = in.readString();
        app_password = in.readString();
        app_username = in.readString();
        store_number = in.readString();
        store_phone = in.readString();
    }

    public static final Creator<FirebaseEmployee> CREATOR = new Creator<FirebaseEmployee>() {
        @Override
        public FirebaseEmployee createFromParcel(Parcel in) {
            return new FirebaseEmployee(in);
        }

        @Override
        public FirebaseEmployee[] newArray(int size) {
            return new FirebaseEmployee[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getApp_password() {
        return app_password;
    }

    public String getApp_username() {
        return app_username;
    }

    public String getStore_number() {
        return store_number;
    }

    public String getStore_phone() {
        return store_phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(app_password);
        parcel.writeString(app_username);
        parcel.writeString(store_number);
        parcel.writeString(store_phone);
    }
    @Override
    public boolean equals(Object o){
        FirebaseEmployee fe = (FirebaseEmployee) o;
        return this.app_username.equals(fe.app_username) && this.app_password.equals(fe.app_password);
    }
    public FirebaseEmployee(String user,String pass){
        this.app_password=pass;
        this.app_username = user;
    }
    public  String toString(){
        return this.name+" "+store_number+" id:"+id;
    }
}

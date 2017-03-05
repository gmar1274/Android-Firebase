package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

/**
 * Created by user on 2017-02-11.
 * THIS IS FOR THE APP LOGIN. FOR STORE WORKER AND EMPLOYEES ACTIVITY. SEE EMPLOYEEACTIVITY.java. for more details.
 * FIREBASE URL: employees/store_number/emp_id/{firebase_employee obj}
 */
public class FirebaseEmployee implements Parcelable {

    private String name, id, app_password, app_username, store_number, store_phone, type, phone;

    protected FirebaseEmployee(Parcel in) {
        name = in.readString();
        id = in.readString();
        app_password = in.readString();
        app_username = in.readString();
        store_number = in.readString();
        store_phone = in.readString();
        type = in.readString();
        phone = in.readString();
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
        parcel.writeString(type);
        parcel.writeString(phone);
    }

    public enum TYPE {OWNER, MANAGER, EMPLOYEE}
    public FirebaseEmployee() {

    }

    /**
     * STORE REGISTRATION - Store OWNER ACCOUNT for the APP
     * @param store
     */
    public FirebaseEmployee(FirebaseStore store, String fullname, TYPE type) {
        this.store_number = store.getStore_number()+"";
        this.name = fullname;
        this.app_username = store.getEmail();
        this.phone = store.getPhone();//defualt leave store phone for personal phone
        this.type = type.toString();
        this.app_password = store.getPassword();
        this.store_phone = store.getPhone();
        DecimalFormat df = new DecimalFormat("####");
        this.id  =  df.format(app_username.hashCode()); //store no preference is id
    }
    /**
     * EMPLOYEE registration
     * The owner will just register the email of the employee and the emnployee can log on and customize their account.
     * @param store
     * @param email
     */
    public FirebaseEmployee(FirebaseStore store, String email) {
        this.store_number = store.getStore_number()+"";
        this.store_phone = store.getPhone();
        this.name = "";
        this.app_username = email;
        this.phone = store_phone;
        this.type = TYPE.EMPLOYEE.toString();
        this.app_password = Encryption.encryptPassword(store_phone); //default password is store_phone
        DecimalFormat df = new DecimalFormat("####");
        this.id  =  df.format(app_username.hashCode());
    }

    /**
     * STORE REGISTRATION DEPRACATED
     * @param store_number
     * @param store_phone
     * @param name
     * @param email
     * @param phone
     * @param pass
     * @param admin_priveledges
     */
    public FirebaseEmployee(String store_number, String store_phone, String name, String email, String phone, String pass, TYPE admin_priveledges) {
        this.store_number = store_number;
        this.name = name;
        this.app_username = email;
        this.phone = phone;
        this.type = admin_priveledges.toString();
        this.app_password = pass;
        this.store_phone = store_phone;
    }


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

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setApp_password(String app_password) {
        this.app_password = app_password;
    }

    public void setApp_username(String app_username) {
        this.app_username = app_username;
    }

    public void setStore_number(String store_number) {
        this.store_number = store_number;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        FirebaseEmployee fe = (FirebaseEmployee) o;
        return this.app_username.equals(fe.app_username) && this.app_password.equals(fe.app_password);
    }

    public FirebaseEmployee(String user, String pass) {
        this.app_password = pass;
        this.app_username = user;
    }

    public String toString() {
        return this.name + " " + store_number + " id:" + id;
    }
}

package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;

/**
 * Created by user on 2016-08-06.
 * POJO for firebase json... stylists/store_number/sty_id/{}
 */
public class Stylist implements Parcelable, Comparable<Stylist>, IMessagingMetaData {
    private String fname, mname, lname,id,phone;
    private int wait;
    private boolean available;
    private double ticket_price;
  //  private Bitmap image;
   // private String image_bytes;//Base64.encode(byte[]);
    private String store_id;
    private String name;
   // private long storeID;
    private int psuedo_wait = 0;
    private String readyBy;


    public void setId(String id) {
        this.id = id;
    }

    public double getTicket_price() {
        return ticket_price;
    }

    public void setTicket_price(double ticket_price) {
        this.ticket_price = ticket_price;
    }

    @Override
    public void setImage_storage_path(String url) {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public String getMname() {
        return mname;
    }

    public String getLname() {
        return lname;
    }

    public String getId() {
        return id;
    }


    /**
     * DEFAULT ADD by store owner.
     * @param id - stylist ID
     * @param name - sty name
     * @param store_id - store number
     */
    public Stylist(String name, String id, String store_id){
        this.convertName(name);
        this.id = id;
        this.store_id = store_id;
        this.phone = "";
        this.wait = 0;
        this.available = false;
    }
    /**
     * THIS WILL BE FOR A STORE NO PREFERENCE ACCOUNT. JUST A DUMMY VARIABLE TO ADD TO FIREBASE URL: stylists/store_number/id/{obj}
     */

    public Stylist(FirebaseStore store, boolean nopref) {
        if (nopref) {
            this.fname = "NO PREFERENCE";
            this.mname = "";
            this.lname = "";
            this.name = "NO PREFERENCE";
            this.id = "-1";
            this.phone = store.getPhone();
            this.wait = 0;
            this.available = true;
        }
    }
    public Stylist(FirebaseStore store, String name, String username, String userPhone){
            this.convertName(name);
            DecimalFormat df = new DecimalFormat("####");
            this.id = df.format(username.hashCode());
            this.phone = userPhone;//default owner phonenumber...no changed to user phone
            this.wait = 0;
            this.available = true;

    }


    /**
     * REGISTER EMPLOYEE BY OWNER
     * Usage: owner logs in and creates a firebase employee with app_username email and default
     * password = store_phone. Then the employee is able to log in and customize their settings.
     * @param emp
     */
    public Stylist(FirebaseEmployee emp ){
       this.convertName(emp.getName());
        DecimalFormat df = new DecimalFormat("####");
        this.id = df.format(emp.getApp_username().hashCode());
        this.phone = emp.getPhone();
        this.wait = 0;
        this.available = false;
    }

    private void convertName(String fullname){
        String[] name = fullname.split(" ");
        if(name.length==3){
            this.fname = name[0];
            this.mname= name[1];
            this.lname = name[2];
        }else if(name.length==2){
            this.fname = name[0];
            this.lname = name[1];
            this.mname = "";
        }else{
            this.fname = name[0];
            this.mname="";
            this.lname="";
        }
        this.name = this.getName();
    }
    //public String getImage_bytes() {return image_bytes;}

    //public void setImage_bytes(String encodedByteArr) {this.image_bytes = encodedByteArr;}

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public static Creator<Stylist> getCREATOR() {
        return CREATOR;
    }
    /**
     * DEBUG TEST STYLIST
     * @param test
     */
    public Stylist(String id,boolean test){
        this.id = id;
    }
    public Stylist(String id){
        this.id=id;
        this.fname=this.mname=this.lname="";
    }
public Stylist(){
    this.id="-1";
    this.fname="No Preference";
    this.mname="";
    this.lname="";
    this.wait=0;
    this.available=true;
   // this.image_bytes=null;
    this.phone=null;
    this.store_id="9091234567";
}

    public Stylist(String id,String fname, String mname, String lname, boolean avail,String pic_byte_arr,String phone,String store_id) {
        this.fname = fname;
        this.lname = lname;
        this.mname=mname;
        this.id=id;
        if(this.id.contains("-1")){
            this.fname="NO PREFERENCE";
            this.mname="";
            this.lname="";
        }
        this.available = avail;
        //this.image=pic;
        //this.image_bytes = pic_byte_arr;
        this.wait=0;
        this.phone = phone;
        this.store_id=store_id;
    }
    public String getStore_id(){
        return this.store_id;
    }
    public void incrementWait(){
        this.wait += 1;
    }

    public String getReadyBy() {
        return readyBy;
    }

    public void setReadyBy(String readyBy) {
        this.readyBy = readyBy;
    }

    public String getPhone(){return  this.phone;}
    public void setPhone(String p){
        this.phone=p;
    }
    //public Bitmap getImage(){return  this.image;}
    public  void setAvailable(boolean available){
        this.available=available;
    }
    public boolean isAvailable(){
    return this.available;
}


    public void setWait(int wait){
    this.wait=wait;
}

    public int getPsuedo_wait() {
        return psuedo_wait;
    }

    public void setPsuedo_wait(int psuedo_wait) {
        this.psuedo_wait = psuedo_wait;
    }

    public int getWait() {
        return this.wait;
    }

    public String getName() {
        if(this.name!=null)return this.name;
        if (this.mname == null || this.mname.length() == 0)
            return this.fname + " " + this.lname;
        else return this.fname + " " + mname + " " + lname;
    }

    /**
     * NOT IMPLEMENTED IN THIS CLASS....STORE ID/images/stylists/id      .....
     * @return
     */
    @Override
    public String getImage_storage_path() {
        return store_id.trim()+"/images/stylists/"+this.id;
    }

    public String toString() {
        return "ID: [" + this.id + "] Name: [" + this.getName()+"] "+"Wait:["+wait+"] "+"PsuedoWait:["+psuedo_wait+"]";
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }

    @Override
    public int compareTo(Stylist stylist) {
        return this.id.compareTo(stylist.id);
    }
    @Override
    public boolean equals(Object o){
        Stylist s = (Stylist) o;
        return this.id.equals(s.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fname);
        dest.writeString(this.mname);
        dest.writeString(this.lname);
        dest.writeString(this.id);
        dest.writeString(this.phone);
        dest.writeInt(this.wait);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeString(this.store_id);
        dest.writeInt(this.psuedo_wait);
        dest.writeString(readyBy);
        dest.writeDouble(ticket_price);
    }
    protected Stylist(Parcel in) {
        this.fname = in.readString();
        this.mname = in.readString();
        this.lname = in.readString();
        this.id = in.readString();
        this.phone = in.readString();
        this.wait = in.readInt();
        this.available = in.readByte() != 0;
        this.store_id = in.readString();
        this.psuedo_wait = in.readInt();
        this.readyBy = in.readString();
        this.ticket_price = in.readDouble();
    }
    public static final Creator<Stylist> CREATOR = new Creator<Stylist>() {
        @Override
        public Stylist createFromParcel(Parcel source) {
            return new Stylist(source);
        }

        @Override
        public Stylist[] newArray(int size) {
            return new Stylist[size];
        }
    };
}
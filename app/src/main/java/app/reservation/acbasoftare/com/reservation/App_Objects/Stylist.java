package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by user on 2016-08-06.
 */
public class Stylist implements Parcelable {
    private String fname, mname, lname,id,phone;
    private int wait;
    private boolean available;
  //  private Bitmap image;
    private byte[] image_bytes;
    private String store_id;

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

    public byte[] getImage_bytes() {
        return image_bytes;
    }

    public void setImage_bytes(byte[] image_bytes) {
        this.image_bytes = image_bytes;
    }

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
    this.image_bytes=null;
    this.phone=null;
    this.store_id="9091234567";
}

    public Stylist(String id,String fname, String mname, String lname, boolean avail,byte[] pic,String phone,String store_id) {
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
        this.image_bytes = pic;
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
    public String getStoreID(){
        return this.store_id;
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
    public int getWait() {
        return this.wait;
    }

    public String getName() {
        if (this.mname == null || this.mname.length() == 0)
            return this.fname + " " + this.lname;
        else return this.fname + " " + mname + " " + lname;
    }

    public String toString() {
        return "ID: [" + this.id + "] Name: [" + this.getName()+"]";
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
       // dest.writeParcelable(this.image, flags);
        dest.writeByteArray(this.image_bytes);
    }

    protected Stylist(Parcel in) {
        this.fname = in.readString();
        this.mname = in.readString();
        this.lname = in.readString();
        this.id = in.readString();
        this.phone = in.readString();
        this.wait = in.readInt();
        this.available = in.readByte() != 0;
        //this.image = in.readParcelable(Bitmap.class.getClassLoader());
       in.readByteArray(this.image_bytes);
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

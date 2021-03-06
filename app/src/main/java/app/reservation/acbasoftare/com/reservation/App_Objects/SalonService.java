package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

import app.reservation.acbasoftare.com.reservation.Utils.Duration;

/**
 * Created by user on 2016-12-04.
 */
public class SalonService implements Parcelable {
    private String name;
    private double price;
    private Duration duration;
    private int id;
    public SalonService(){

    }
    public SalonService(int id,String name,double price, Duration duration){
        this.id=id;
        this.name=name;
        this.price=price;
        this.duration=duration;
    }
    public String getName(){
        return this.name;
    }
    public double getPrice(){
        return this.price;
    }
    public Duration getDuration(){
        return  this.duration;
    }
    public int getId(){
        return this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(price);
        dest.writeParcelable(this.duration,flags);
        dest.writeInt(this.id);
    }

    protected SalonService(Parcel in) {
        this.name = in.readString();
        this.price = in.readDouble();
        this.duration = (Duration) in.readSerializable();
        this.id = in.readInt();
    }
public String getFormattedPrice(){
    DecimalFormat df = new DecimalFormat("$ #,##0.00");
    return df.format(price);
}
    public static final Creator<SalonService> CREATOR = new Creator<SalonService>() {
        @Override
        public SalonService createFromParcel(Parcel source) {
            return new SalonService(source);
        }

        @Override
        public SalonService[] newArray(int size) {
            return new SalonService[size];
        }
    };
    public String toString(){
        return this.getName();
    }
    /**
     * FIREBASE GETTERS
     */

    //end getters

}

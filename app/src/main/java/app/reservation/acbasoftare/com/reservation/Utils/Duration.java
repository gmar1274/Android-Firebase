package app.reservation.acbasoftare.com.reservation.Utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.IntProperty;
import android.util.Log;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by user on 12/6/16.
 */
public class Duration implements Parcelable {
    private String time;//format of hh:mm
    /**This method only accepts HH:mm */
    public  Duration(String time){
        //Log.d("timeee: ",time);
       this.time=time;
    }
    public String getDuration(){
        return this.toString();
    }
    public String toString(){
        String[] arr = time.split(":");
        DecimalFormat df = new DecimalFormat("#0");
        int hour =Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);
        if(hour==0){
            return df.format(min)+" mins";
        }else {
            return df.format(hour) + " hrs " + df.format(min) + " mins";
        }
    }
    public long getMilliSeconds(){
        String[] arr = time.split(":");
        long sum=0;
        if(arr[0]!=null) {
            int hour = Integer.parseInt(arr[0]);
            sum += Utils.hourToseconds(hour)*1000;
        }
        if(arr[1]!=null){
            int min = Integer.parseInt(arr[1]);
            sum+=Utils.minToseconds(min)*1000;
        }
        return sum;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.time);
    }

    protected Duration(Parcel in) {
        this.time = in.readString();
    }

    public static final Creator<Duration> CREATOR = new Creator<Duration>() {
        @Override
        public Duration createFromParcel(Parcel source) {
            return new Duration(source);
        }

        @Override
        public Duration[] newArray(int size) {
            return new Duration[size];
        }
    };
}

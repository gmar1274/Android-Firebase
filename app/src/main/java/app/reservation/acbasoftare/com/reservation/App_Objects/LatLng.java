package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 1/18/17.
 */

public class LatLng implements Parcelable {
   public double latitude,longitude;

    protected LatLng(Parcel in){
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }
    public static final Creator<LatLng> CREATOR = new Creator<LatLng>() {

        @Override
        public LatLng createFromParcel(Parcel parcel) {
            return new LatLng(parcel);
        }

        @Override
        public LatLng[] newArray(int i) {
            return new LatLng[i];
        }
    };
    public LatLng(){

    }
    public String toString(){return "Lat: "+latitude+", Long: "+longitude;}
    public LatLng(double lat,double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
            parcel.writeDouble(latitude);
            parcel.writeDouble(longitude);
    }
}

package app.reservation.acbasoftare.com.reservation.App_Services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 2016-11-07.
 */
public class GPSLocation extends Service implements LocationListener, Parcelable {
    private Context mContext;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    // Declaring a Location Manager
    protected LocationManager locationManager;
    private Activity activity;
    private boolean error;

    public GPSLocation(Activity a) {
        this.mContext = a.getApplicationContext();
        this.activity=a;
        getLocation();

    }

    protected GPSLocation(Parcel in) {
        isGPSEnabled = in.readByte() != 0;
        isNetworkEnabled = in.readByte() != 0;
        canGetLocation = in.readByte() != 0;
        location = in.readParcelable(Location.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        error = in.readByte() != 0;
    }

    public static final Creator<GPSLocation> CREATOR = new Creator<GPSLocation>() {
        @Override
        public GPSLocation createFromParcel(Parcel in) {
            return new GPSLocation(in);
        }

        @Override
        public GPSLocation[] newArray(int size) {
            return new GPSLocation[size];
        }
    };

    public boolean isPermissionError(){return  this.error;}
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    activity.finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public Location getLocation() {
        try {
            if(location!=null)return location;
            error=false;
            locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

                this.canGetLocation = false;
                return null;
            }
            if (ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
                this.canGetLocation = true;
                return null;
            }


            this.canGetLocation = true;

            // First get location from Network Provider
            if (isNetworkEnabled) {

                Log.e("Network", "Network");

                if (locationManager != null) {


                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
            // if GPS Enabled get lat/long using GPS Services
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.e("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager
                                           .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
            }


        } catch (Exception e) {
            Toast.makeText(this.activity,"Please provide permission for GPS.",Toast.LENGTH_LONG).show();
            error=true;
            e.printStackTrace();
        }finally {
            return location;
        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location loc) {
        if(location==null){
            this.location = this.getLocation();
            return;
        }
        if(this.location.getLatitude()==loc.getLatitude() && this.location.getLongitude()==loc.getLongitude())return;///equal so dismiss
        this.location = loc;//might need to update google maps
        /*if(ma.mapview != null && ma.mainView!=null){//update map
            ma.user_loc = this.location;
            //MainActivity.showGoogleMaps(MainActivity.mainView,MainActivity.store_list);
        }*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }



    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(GPSLocation.this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (isGPSEnabled ? 1 : 0));
        parcel.writeByte((byte) (isNetworkEnabled ? 1 : 0));
        parcel.writeByte((byte) (canGetLocation ? 1 : 0));
        parcel.writeParcelable(location, i);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeByte((byte) (error ? 1 : 0));
    }
}
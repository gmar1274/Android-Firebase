package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.app.Activity;
import android.os.AsyncTask;

import app.reservation.acbasoftare.com.reservation.App_Services.GPSLocation;

/**
 * Created by user on 2016-11-09.
 */
public class GPSTask extends AsyncTask<Void, Void, Void> {
    private Activity a ;
    private GPSLocation gps;
    public GPSTask(Activity a){
        this.a=a;
    }

    @Override
    protected Void doInBackground(Void... params) {

        gps= new GPSLocation(this.a);
         return null;
     }
}

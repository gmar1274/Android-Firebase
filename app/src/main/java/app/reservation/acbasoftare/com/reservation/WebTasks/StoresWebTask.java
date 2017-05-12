package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.R;

import static com.google.api.client.http.HttpMethods.HEAD;

/**
 * Created by user on 2016-08-06.
 */
public class StoresWebTask extends AsyncTask<String, Void, String> {
    private ProgressBar progressBar;
    private View rootView;
    //private MapView mapview = MainActivity.mapview;//static use for on create aka when screen oriteation is changed
    //private GoogleMap google_map = MainActivity.google_map;
private MainActivity ma;
    public StoresWebTask(MainActivity ma, View root) {
        this.rootView = root;
       // ma.mainView = this.rootView;
        this.ma =ma;

        if (ma.store_list == null) {//connect to database
            ma.store_list = new ArrayList<FirebaseStore>();
        }

    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            int miles = Integer.parseInt(arg0[0])+1;
            ma.miles=miles;
            String link = "http://acbasoftware.com/pos/store.php";
            String data = URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastorelistacba"), "UTF-8");
            data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(ma.user_loc.getLatitude() + "", "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(ma.user_loc.getLongitude() + "", "UTF-8");
            data += "&" + URLEncoder.encode("radius", "UTF-8") + "=" + URLEncoder.encode(miles + "", "UTF-8");//meters

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {


        try {

            JSONObject jObject = new JSONObject(result);
            JSONArray jArray = jObject.getJSONArray("store");
           for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    double lat = oneObject.getDouble("lat");
                    double lon = oneObject.getDouble("lon");
                    LatLng loc2 = new LatLng(lat, lon);//meters

                    String name = oneObject.getString("Store");
                    String addr = oneObject.getString("Address");
                    String citystate = oneObject.getString("CityState");

                    String phone = oneObject.getString("phone");
                    double miles_away = oneObject.getDouble("distance");
                    double ticket_price = (oneObject.getDouble("ticket_price"));
                    String open = oneObject.getString("open_time");
                    String close = oneObject.getString("close_time");
                    double cprice = (oneObject.getDouble("reservation_price"));
                  //  ma.store_list.add(new FirebaseStore(name, addr, citystate, phone, lat, lon,i,miles_away,ticket_price,open,close,cprice));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }///////end for


           // if (MainActivity.lv == null) {
                //MainActivity.lv =

                ListView lv = (ListView) rootView.findViewById(R.id.fragment_listview);

            //}
           // if (MainActivity.la == null) {
             MainActivity.ListViewAdapter la = new MainActivity.ListViewAdapter(ma, ma.store_list);
               lv.setAdapter(la);
            //}


            ma.showGoogleMaps(rootView, ma.store_list);
            //if (MainActivity.srl.isRefreshing()) MainActivity.srl.setRefreshing(false);
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


}

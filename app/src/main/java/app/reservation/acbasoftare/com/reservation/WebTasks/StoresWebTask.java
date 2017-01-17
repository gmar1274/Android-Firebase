package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.util.Log;
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
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.UnitConversion;

/**
 * Created by user on 2016-08-06.
 */
public class StoresWebTask extends AsyncTask<String, Void, String> {
    private ProgressBar progressBar;
    private View rootView;
    //private MapView mv = MainActivity.mv;//static use for on create aka when screen oriteation is changed
    //private GoogleMap gm = MainActivity.gm;

    public StoresWebTask(View root) {
        this.rootView = root;
        MainActivity.mainView = this.rootView;

        if (MainActivity.store_list == null) {//connect to database
            MainActivity.store_list = new ArrayList<Store>();
        }

    }

    protected void onPreExecute() {
        showProgressBar(true, rootView);
    }

    private void showProgressBar(boolean show, View rootView) {
        progressBar = (ProgressBar) rootView.findViewById(R.id.fragment_progress_bar);
        if (!show) {
            progressBar.setVisibility(View.GONE);
            return;
        }
        progressBar.setIndeterminate(show);
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            int miles = Integer.parseInt(arg0[0])+1;
            MainActivity.miles=miles;
            String link = "http://acbasoftware.com/pos/store.php";
            String data = URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastorelistacba"), "UTF-8");
            data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(MainActivity.user_loc.getLatitude() + "", "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(MainActivity.user_loc.getLongitude() + "", "UTF-8");
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
                    BigDecimal ticket_price = new BigDecimal(oneObject.getDouble("ticket_price"));
                    String open = oneObject.getString("open_time");
                    String close = oneObject.getString("close_time");
                    BigDecimal cprice = new BigDecimal(oneObject.getDouble("reservation_price"));
                    MainActivity.store_list.add(new Store(name, addr, citystate, phone, lat, lon,i,miles_away,ticket_price,open,close,cprice));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }///////end for


            if (MainActivity.lv == null) {
                MainActivity.lv = (ListView) rootView.findViewById(R.id.fragment_listview);
            }
            if (MainActivity.la == null) {
                MainActivity.la = new MainActivity.ListViewAdapter(rootView.getContext(), MainActivity.store_list);
                MainActivity.lv.setAdapter(MainActivity.la);
            }
            showProgressBar(false, this.rootView);

            MainActivity.showGoogleMaps(rootView, MainActivity.store_list);
            //if (MainActivity.srl.isRefreshing()) MainActivity.srl.setRefreshing(false);
        } catch (JSONException e) {
            TextView tv = (TextView) rootView.findViewById(R.id.fragment_text_view);
            tv.setText("Network Error. Connection is down. Please be patient while the problem is being fixed.");
            e.printStackTrace();
        }
    }


}

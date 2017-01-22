package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.Utils.Duration;

/**
 * Created by user on 12/6/16.
 */
public class SalonServiceWebTask extends AsyncTask<String,Void,String> {

    private View rootView;
    private Store store;
    private final String link = "http://acbasoftware.com/pos/services.php";
    private int pos;
    public SalonServiceWebTask(View rv,Store store,final int pos){
        this.rootView = rv;this.store=store;
        this.pos = pos;
    }
    @Override
    protected String doInBackground(String... arg0) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String id = arg0[0];//param 1 is store_id
            String data = URLEncoder.encode("store_services", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastoreservicesacba"), "UTF-8");
            data += "&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(id , "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(sdf.format(new Date()) , "UTF-8");


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

            JSONObject jObject=new JSONObject(result);
            JSONArray jArray=jObject.getJSONArray("store_services");

            for(int i=0; i < jArray.length(); i++)
            {
                try {
                    JSONObject oneObject=jArray.getJSONObject(i);
                    // Pulling items from the array

                    BigDecimal price=new BigDecimal(oneObject.getDouble("price"));
                    String name=oneObject.getString("name");
                    int id=oneObject.getInt("id");
                    String duration=oneObject.getString("duration");///string format
                    SalonService ss=new SalonService(id, name, price, new Duration(duration));
                    store.addService(ss);
                } catch(JSONException e) {
                e.printStackTrace();
                }
            }///////end for
            ////////////////////get Reservations
            JSONArray jArray2=jObject.getJSONArray("reservations");
            store.initializeReservations();

            for(int i=0; i < jArray2.length(); i++)
            {
                try {
                    JSONObject oneObject=jArray2.getJSONObject(i);
                    Stylist sty = store.getStylistHashMap().get(oneObject.getString("stylist_id"));///should guarentee stylist
                    if(sty.getId().equalsIgnoreCase("-1")){continue;}
                    String start_time = oneObject.getString("start_time");
                    String end_time = oneObject.getString("end_time");
                    store.getReservations().setDateTime(sty,start_time,end_time);//sets appointments//or updates the appointment
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }///////end for

            MainActivity.store_list.remove(MainActivity.selectedPosition);
            MainActivity.store_list.add(pos,store);
            MainActivity.updateRVServices(store);//update GUI for services...because I had the arraylist for stylist but need services
            //update Calendar
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

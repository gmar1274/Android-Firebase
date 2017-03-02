package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;

/**
 * Created by user on 2017-02-17.
 */
public class SMSWebTask extends AsyncTask<Void,String,String> {
    private final String url = "http://www.acbasoftware.com/pos/sms.php";
    private  Ticket t;
    private FirebaseStore store;
    public SMSWebTask(FirebaseStore store, Ticket t){
        this.t = t;
        this.store = store;
    }
    @Override
    protected String doInBackground(Void... voids) {
        try {
            String data = URLEncoder.encode("sms", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbasmsacba"), "UTF-8");
            data += "&" + URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(store.getName().toUpperCase(), "UTF-8");
            data += "&" + URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(t.stylist.toUpperCase() + "", "UTF-8");
            data += "&" + URLEncoder.encode("ticket", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(t.unique_id), "UTF-8");//meters
            data += "&" + URLEncoder.encode("readyBy", "UTF-8") + "=" + URLEncoder.encode(t.readyBy, "UTF-8");//meters
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(t.phone, "UTF-8");//meters
            Log.e("in sms..","phone:"+t.phone+" "+t.readyBy+" "+t);
            URL url = new URL(this.url);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb=new StringBuilder();
            String line=null;

            // Read Server Response
            while((line=reader.readLine()) != null) {
                sb.append(line);
                break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onPostExecute(String result){

    }
}

package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.ParamPair;

import static android.R.attr.data;

/**
 * Created by user on 1/9/17.
 */

public class WebService {
    static InputStream is=null;
    static JSONObject jObj=null;
    static String json="";
    public static final String storeURL="http://acbasoftware.com/pos/store.php";
public static final String createChargeURL = "http://acbasoftware.com/pos/createCharge.php";
    public static final String storeLoginURL = "http://acbasoftware.com/pos/store_login.php";
    // constructor
    public WebService() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url_string, ArrayList<ParamPair> params) {

        // Making HTTP request
        try {

            URL url=new URL(url_string);
            URLConnection conn=url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());
            String data = "";
            for(ParamPair p : params){
                data += p.getPostParameter()+"&";
            }
            data = data.substring(0,data.length()-1);//hopefully deletes the last &... key=val& ...
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
            return new JSONObject(sb.toString());
        } catch(IOException e) {
            e.printStackTrace();
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}

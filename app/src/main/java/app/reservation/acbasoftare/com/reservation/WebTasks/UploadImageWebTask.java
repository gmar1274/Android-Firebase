package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.R;

import static android.R.attr.data;
import static android.util.Base64.encode;
import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.miles;
import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.rootView;

/**
 * Created by user on 12/17/16.
 */
public class UploadImageWebTask extends AsyncTask<String, Void, String> {
    private String link = "http://acbasoftware.com/pos/upload_image.php";
    private Bitmap image;
    public UploadImageWebTask(Bitmap im){
        this.image=im;
    }
    @Override
    protected String doInBackground(String... arg0) {

        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,o);
            byte[] i = o.toByteArray();
            String img =Base64.encodeToString(i,Base64.DEFAULT);
            Log.e("pic::",img);
            String data = URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbaimageacba"), "UTF-8");
            data += "&" + URLEncoder.encode("stylist_id", "UTF-8") + "=" + URLEncoder.encode(EmployeeActivity.getStylistID(), "UTF-8");
            data += "&" + URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(img, "UTF-8");
            data += "&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(EmployeeActivity.store_id, "UTF-8");//meters

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
        Log.e("resultttt:", result);
        Toast.makeText(EmployeeActivity.employeeActivity,"Picture updated!",Toast.LENGTH_LONG).show();
    }


}

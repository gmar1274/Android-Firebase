package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;

public class Login extends AsyncTask<String, Void, String> {
    private Activity a;
    private SharedPreferences pref;
    private EditText passView;
    private AutoCompleteTextView emailView;
    private final  String link = "http://acbasoftware.com/pos/login.php";
    private  enum LOGIN_TYPE{USER,STYLIST};
    private ProgressDialog pd;
    public Login(Activity a) {
        this.a = a;
        pd = ProgressDialog.show(this.a, "Authenticating", "Please Wait...", true, false);
        pd.show();
        pref = null;
    }

    public Login(Activity a, AutoCompleteTextView mEmailView, EditText mPasswordView, SharedPreferences pref) {
        this.emailView = mEmailView;
        this.passView = mPasswordView;
        this.pref = pref;
        this.a = a;
    }


    protected void onPreExecute() {
        if (pd == null) {
            pd = ProgressDialog.show(this.a, "Authenticating", "Please Wait...", true, false);
            pd.show();
        }
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String username = arg0[0];
            String password = arg0[1];

            String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            data += "&" + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbaloginacba"), "UTF-8");

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
            e.printStackTrace();
            return "";
        }

    }

    @Override
    protected void onPostExecute(String result) {
        this.pd.dismiss();
       try {
           JSONObject response =new JSONObject(result);
           JSONArray arr= response.getJSONArray("status");
           if(arr.length()==0){
               error();
               return;
           }
           for(int i=0; i<arr.length(); ++i){
               JSONObject obj = arr.getJSONObject(i);
               boolean success = Boolean.parseBoolean(obj.getString("success"));
               String login_type=obj.getString("login");
               String store_id=obj.getString("store_id");
               String stylist_id= obj.getString("stylist_id");
               if(success && isNormalUser(login_type) ){
                   if (pref != null) {
                      // pref.edit().putString(PREF_USERNAME, emailView.getText().toString()).putString(PREF_PASSWORD, passView.getText().toString()).commit();
                   }
                   a.startActivity(new Intent(a,MainActivity.class));
               }else if(success && isStylist(login_type)){
                   if (pref != null) {
                      // pref.edit().putString(PREF_USERNAME, emailView.getText().toString()).putString(PREF_PASSWORD, passView.getText().toString()).commit();
                   }
                   Intent intent = new Intent(a, EmployeeActivity.class);
                   intent.putExtra("store_id",store_id);
                   intent.putExtra("stylist_id",stylist_id);
                   a.startActivity(intent);
               }else
               {
                   error();
                   return;
               }
           }
           /*
           if (pref != null) {
                pref.edit().putString(LoginActivity.PREF_USERNAME, emailView.getText().toString()).putString(LoginActivity.PREF_PASSWORD, passView.getText().toString()).commit();
            }//getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent intent = new Intent(this.a, MainActivity.class);
            this.a.startActivity(intent);*/
        }catch(Exception e){
            error();
            e.printStackTrace();
        }
    }

    private boolean isStylist(String login) {
        return LOGIN_TYPE.STYLIST.toString().toLowerCase().equalsIgnoreCase(login.toLowerCase());
    }

    private boolean isNormalUser(String login) {
       return LOGIN_TYPE.USER.toString().toLowerCase().equalsIgnoreCase(login.toLowerCase());
    }

    private void error() {
        emailView.setError("Username may be incorrect");
        passView.setError("Password may be incorrect");}
}
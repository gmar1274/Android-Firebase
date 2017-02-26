package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import app.reservation.acbasoftare.com.reservation.App_Objects.ParamPair;

/**
 * Created by user on 1/9/17.
 */
public class WebService {
    public static final String storeURL = "http://acbasoftware.com/pos/store.php";
    public static final String createChargeURL = "https://54.153.34.48/createCharge.php?";//"http://acbasoftware.com/pos/createCharge.php";
    public static final String storeLoginURL = "http://acbasoftware.com/pos/store_login.php";
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    /////////////////
    final private HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }
    };

    // constructor
    public WebService() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url_string, ArrayList<ParamPair> params) {

        // Making HTTP request
        try {


            String data = "";
            for (ParamPair p : params) {
                data += p.getPostParameter() + "&";
            }
            data = data.substring(0, data.length() - 1);//hopefully deletes the last &... key=val& ...
            URL url = new URL(url_string);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());


            wr.write(data);
            wr.flush();
            wr.close();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            reader.close();
            return new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    ///////////////
    public JSONObject makeHTTPSConnection(String url_string, ArrayList<ParamPair> params) {
        // Making HTTP request
    HttpsURLConnection conn = null;
        try {

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };
// Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
// Now you can access an https URL without having the certificate in the truststore
            URL url = new URL(url_string);


            //////
            String data = "";
            for (ParamPair p : params) {
                data += p.getPostParameter() + "&";
            }
            data = data.substring(0,data.length()-1);//hopefully deletes the last &... key=val& ...

            ////set up post request^
            //URLConnection conn = url.openConnection();
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setSSLSocketFactory(sc.getSocketFactory());


            conn.setRequestProperty( "Accept", "*/*" );
            conn.setFixedLengthStreamingMode(data.getBytes().length);
            //conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Content-Language", "en-US");
          ///write
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            wr.close();
           // Log.e("RESPONSE CODE: ","RESPONSE CODE::: "+conn.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            reader.close();
            Log.e("POST PARAMS: ","data: "+data+"\n\nRESPONSE CODE: "+conn.getResponseCode()+"\n");
            Log.e("SERVER RESPONSE:: ",""+ sb.toString());
            return new JSONObject(sb.toString());
        } catch (IOException e) {
             InputStream is = conn.getErrorStream();
            Log.e("ERR Stream:: ", is.toString());
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

}

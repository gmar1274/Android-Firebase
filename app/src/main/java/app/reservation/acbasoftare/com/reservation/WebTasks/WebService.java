package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
<<<<<<< HEAD
=======
import java.io.DataOutputStream;
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
<<<<<<< HEAD
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
=======
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
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
<<<<<<< HEAD
    public static final String createChargeURL = "https://54.153.34.48/createCharge.php/?";//"http://acbasoftware.com/pos/createCharge.php";
    public static final String storeLoginURL = "http://acbasoftware.com/pos/store_login.php";
=======
    public static final String createChargeURL = "https://ec2-54-153-34-48.us-west-1.compute.amazonaws.com/createCharge.php";//54.153.34.48/createCharge.php";//"http://acbasoftware.com/pos/createCharge.php";
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
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a

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
<<<<<<< HEAD
            final TrustManager[] trustAllCerts = new TrustManager[]{
=======
            TrustManager[] trustAllCerts = new TrustManager[]{
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
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
<<<<<<< HEAD
            final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            };
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
=======
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
// Now you can access an https URL without having the certificate in the truststore
            URL url = new URL(url_string);

            //////
<<<<<<< HEAD
            String data = "?";
            for (ParamPair p : params) {
                data += p.getPostParameter() + "&";
            }
            data = data.substring(0,data.length()-1);//hopefully deletes the last '&'... key=val& ...
=======
            String data = "";
            for (ParamPair p : params) {
                data += p.getPostParameter() + "&";
            }
            data = data.substring(0,data.length()-1);//hopefully deletes the last &... key=val& ...
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a

            ////set up post request^
            //URLConnection conn = url.openConnection();
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setSSLSocketFactory(sc.getSocketFactory());
<<<<<<< HEAD
            conn.setHostnameVerifier(DO_NOT_VERIFY);
            conn.setRequestProperty( "Accept-Language", "en-us,en;q=0.5" );
            conn.setFixedLengthStreamingMode(data.getBytes("UTF-8").length);
            //conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
=======


            conn.setRequestProperty( "Accept", "*/*" );
            conn.setFixedLengthStreamingMode(data.getBytes("UTF-8").length);
            //conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8");
            conn.setRequestProperty("Accept", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Accept-Charset", "UTF-8");

>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Content-Language", "en-US");
           // conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
<<<<<<< HEAD
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Length",String.valueOf(data.getBytes("UTF-8")));
            conn.connect();
            Log.e("WEB URL IS:: ","URL : "+conn.getURL());
          ///write
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            Log.e("OUTSTREAM:: ",conn.getOutputStream().toString()+"\n"+conn.getURL().getAuthority());

=======
            conn.setInstanceFollowRedirects(true);

            conn.connect();
          ///write
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
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
<<<<<<< HEAD
        }
        catch (IOException e) {
=======
        } catch (IOException e) {
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
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
<<<<<<< HEAD
        catch(GeneralSecurityException e){
            e.printStackTrace();
        }
=======
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
        return null;
    }

}

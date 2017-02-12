package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.stripe.android.TokenCallback;
import com.stripe.android.exception.APIConnectionException;
import com.stripe.android.exception.APIException;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.exception.CardException;
import com.stripe.android.exception.InvalidRequestException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.ReservationActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;

/**
 * Created by user on 12/8/16.
 */
public class ReservationWebTask  extends AsyncTask<String,Void,String> {
    private final String pk = "pk_test_txeeWRDZ5WJKfYiMa9ItQT9I", sk = "sk_test_JnPKAFeuBTwyRKUQJgzvpZBP";
    private final String link="http://acbasoftware.com/pos/appointment.php";
    private ReservationActivity ra;
   //private Charge charge;
    private Card card;
    private String phone;

   /* public ReservationWebTask(ReservationActivity ra, Card card,Charge charge, String phone) {
        this.ra=ra;
        this.card=card;
        this.phone=phone;
        this.charge=charge;
    }
    */

    @Override
    protected String doInBackground(String... arg0) {

        try {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String id=ra.getStore().getPhone(); //param 1 is store_id
            String datetime_start=sdf.format(ra.getTimeSet().getLowerBound()); ///start_time
            String datetime_end=sdf.format(ra.getTimeSet().getUpperBound());////
            String stylist=ra.getStylist().getId();
            String customer_id=ra.getCustomer().getID();//FB id if available
            String customer_name=ra.getCustomer().getName();
            String service_id=ra.getSalonService().getId() + "";
            String notes=ra.getCommentSection();

            String data=URLEncoder.encode("reservation", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbareservationacba"), "UTF-8");
            data+="&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
            data+="&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(sdf.format(new Date()), "UTF-8");
            data+="&" + URLEncoder.encode("start_time", "UTF-8") + "=" + URLEncoder.encode(datetime_start, "UTF-8");
            data+="&" + URLEncoder.encode("end_time", "UTF-8") + "=" + URLEncoder.encode(datetime_end, "UTF-8");
            data+="&" + URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(stylist, "UTF-8");
            data+="&" + URLEncoder.encode("customer_id", "UTF-8") + "=" + URLEncoder.encode(customer_id, "UTF-8");

            data+="&" + URLEncoder.encode("customer_name", "UTF-8") + "=" + URLEncoder.encode(customer_name, "UTF-8");
            data+="&" + URLEncoder.encode("service_id", "UTF-8") + "=" + URLEncoder.encode(service_id, "UTF-8");
            data+="&" + URLEncoder.encode("notes", "UTF-8") + "=" + URLEncoder.encode(notes, "UTF-8");
            data+="&" + URLEncoder.encode("customer_phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");


            URL url=new URL(link);
            URLConnection conn=url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr=new OutputStreamWriter(conn.getOutputStream());

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
            return sb.toString();
        } catch(Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String result) {

        try {

            JSONObject jObject=new JSONObject(result);
            try {
                // Pulling items from the array
                if(jObject.getJSONObject("status").getString("success").equalsIgnoreCase("true")) {
                    Toast.makeText(this.ra.getApplicationContext(), "Reservation was made. Thank you!!", Toast.LENGTH_LONG).show();
                    ra.createReservationTicket(); //dismiss dialog
                } else {
                    Toast.makeText(this.ra.getApplicationContext(), "Error. No reservation was made.", Toast.LENGTH_LONG).show();
                    //my guess is that the time was taken by someone else...
                    refundMoney();  //db problem refund money
                    ra.removeCurrentTimeChoice();
                }

            } catch(JSONException e) {
                e.printStackTrace();
            }


        } catch(JSONException e) {
            e.printStackTrace();
        }
    }
    private void refundMoney() {
        com.stripe.android.Stripe stripe = null;

        try {
            stripe = new com.stripe.android.Stripe(pk);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
      //  com.stripe.Stripe.apiKey = sk;
        if (stripe != null) {
            stripe.createToken(card, new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                           ///
                        }

                        public void onSuccess(final Token token) {
                            Thread t = new Thread() {
                                public void run() {
                                    /*try {
                                      //  charge.refund();
                                    } catch(AuthenticationException e) {
                                        e.printStackTrace();
                                    } catch(InvalidRequestException e) {
                                        e.printStackTrace();
                                    } catch(APIConnectionException e) {
                                        e.printStackTrace();
                                    } catch(CardException e) {
                                        e.printStackTrace();
                                    } catch(APIException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            };
                            t.start();
                        }
                    }
            );
        }


    }

}

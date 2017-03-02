package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Customer;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.Reservation;
import app.reservation.acbasoftare.com.reservation.App_Objects.ReservationDetails;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 12/13/16.
 */
public class StylistWebTaskAppointments extends AsyncTask<String, Void, String> {
        private ProgressBar progressBar;
        private String store_id,stylist_id;
        private View rootView;
        private final String link = "http://acbasoftware.com/pos/stylist.php";
        private ListView lvv;
        private  ExpandableListView lv;
    private EmployeeActivity ea;
        public StylistWebTaskAppointments(EmployeeActivity ea, View rootView, ExpandableListView lv) {
          //  store_id =ea.getStoreID();
            //stylist_id=ea.getStylistID();
            this.rootView= rootView;
            this.ea = ea;
        }

        protected void onPreExecute() {
            showProgressBar(true,rootView);
        }

        private void showProgressBar(boolean show, View rootView) {
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_emp_act);
            if (show == false) {
                progressBar.setIndeterminate(false);
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setIndeterminate(true);
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String data = URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastylistlistacba"), "UTF-8");
                data += "&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
                data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(sdf.format(date), "UTF-8");
                data += "&" + URLEncoder.encode("stylist_id", "UTF-8") + "=" + URLEncoder.encode(stylist_id, "UTF-8");
                data += "&" + URLEncoder.encode("employee_activity", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
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
                return new String("Exception: " + e.getMessage());////no store id...
            }

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jObject = new JSONObject(result);
                JSONArray arr = jObject.getJSONArray("stylist");
                Stylist stylist = null;
                for(int i = 0; i <arr.length(); ++i){
                    JSONObject jobj = arr.getJSONObject(i);
                    // Pulling items from the array
                    String fname = jobj.getString("fname");
                    String mname = jobj.getString("mname");
                    String lname = jobj.getString("lname");
                    String stylist_id = jobj.getString("stylist_id");
                    boolean available = jobj.getString("available").contains("1");//check to confirm 0 is false and 1 is true
                    String qrimage = jobj.getString("image");
                   // byte[] qrimageBytes = Base64.decode(qrimage.getBytes(), Base64.DEFAULT);
                  //  Bitmap bmp = BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length);//Utils.resize(BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length),100,100);
                    String phone = jobj.getString("phone");
                    // Bitmap myBitmap = jobj.getby
                    //Stylist s = new Stylist(stylist_id,fname,mname,lname,available,qrimage,phone,ea.getStoreID());
                    //stylist = s;
                }
                ///////////////////////read stylist
                Reservation res = new Reservation(stylist);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONArray jArray = jObject.getJSONArray("appointments");///get tickets and id
                for (int i = 0; i < jArray.length(); ++i) {
                    try {
                        JSONObject obj = jArray.getJSONObject(i);
                        String start = obj.getString("start_time");
                        String end = obj.getString("end_time");
                        String customer_id=obj.getString("customer_id");
                        String customer_name=obj.getString("customer_name");
                        String notes = obj.getString("notes");
                        String service_name = obj.getString("service_name");
                        String phone = obj.getString("phone");
                        Customer c = new Customer(customer_name);
                        c.setID(customer_id);
                        c.setPhone(phone);
                        c.setName(customer_name);
                      // Date date = null;
                        Date start_time=null;
                        Date end_time=null;

                        try {
                          //  date = sdf.parse(start);
                            start_time=sdf.parse(start);
                            end_time = sdf.parse(end);
                        } catch(ParseException e) {
                            e.printStackTrace();
                        }

                        ReservationDetails rd = new ReservationDetails(new TimeSet(start_time,end_time),service_name,c,notes);
                        res.setDateTime(stylist,start,end,rd);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//end for
               // EmployeeActivity.reservation = null;
               // EmployeeActivity.reservation = res;

            } catch (JSONException e1) {
                e1.printStackTrace();
                return;
            }
//            ArrayList<TimeSet> headers = EmployeeActivity.reservation.getDayTimes(new Date());
//            HashMap<TimeSet,ReservationDetails> child = EmployeeActivity.reservation.getReservationDetailsHashMap();
//            ExpandableListViewAdapter lva = new ExpandableListViewAdapter(ea,headers,child);
//            EmployeeActivity.lva=lva;
//            lv = (ExpandableListView)rootView.findViewById(R.id.expandable_List_view);
//            lv.setAdapter(lva);
//            showProgressBar(false, this.rootView);
        }
    }
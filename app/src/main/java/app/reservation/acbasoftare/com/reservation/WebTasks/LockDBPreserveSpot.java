package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.Manifest;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2016-11-18.
 */
public class LockDBPreserveSpot extends AsyncTask<String, Void, String> {
private final String site="http://acbasoftware.com/pos/lockdb.php";
    private  boolean INSERT,DELETE,UPDATE;

    private String store_id,ticket,stylist,name,phone;

    public LockDBPreserveSpot() {
            INSERT=false;
            DELETE=false;
            UPDATE=false;
        }
    private String updateTicket(String[] param) throws UnsupportedEncodingException {
        store_id=param[0];
         ticket = param[1];
         name = param[2];
         stylist=param[3];
         phone=MainActivity.phone;
        //no email.. phone we can get from the OS
        String data="";
        data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
        data += "&" + URLEncoder.encode("lock", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbalockacba"), "UTF-8");
        data += "&" + URLEncoder.encode("ticket", "UTF-8") + "=" + URLEncoder.encode(ticket, "UTF-8");
        data += "&" + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
        data += "&" + URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(stylist, "UTF-8");
        data += "&" + URLEncoder.encode("update", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
        data += "&" + URLEncoder.encode("cust_phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

        return data;
    }

    /****
     *
     * @param arg0 - is the store id
     *             there can be multiple params, store_id,ticket number, name, stylist id
     *             This method will either insert a new record, then either update or delete this new record
     *             if param arg0 length is 1 then insert new
     *             if param arg0 is 2 length then delete ticket
     *             else update that ticket
     * @return
     */
        @Override
        protected String doInBackground(String... arg0) {
            try {//insert new ticket blank just ticket number and istore_id
                String store_id=arg0[0];
                String link = this.site;
                String data="";
                final Stylist stylist_obj=  MainActivity.stylists_list.get(MainActivity.stylist_postion);
                if(arg0.length==1) {//////////case 1: only 1 param. Make a arbritarty insert ticket
                    //no need to specify stylist id
                    INSERT=true;
                    this.store_id=store_id;
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(this.store_id, "UTF-8");
                    data += "&" + URLEncoder.encode("lock", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbalockacba"), "UTF-8");
                    data += "&" + URLEncoder.encode("insert", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8");
                    data += "&" + URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(stylist_obj.getID(), "UTF-8");//get selected stylisy

                }else if(arg0.length==2){//case 2: delete the ticket. 2 params, store and ticket_number
                    DELETE=true;
                    data = deleteTicket(arg0);
                }
                else {//case 3: ticket was paid for. Update the correct stylist
                    UPDATE=true;
                    data=updateTicket(arg0);
                }
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

    private String deleteTicket(String[] param) throws UnsupportedEncodingException {
        String store_id=param[0];
        String ticket = param[1];
        String data="";
        data = URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
        data += "&" + URLEncoder.encode("lock", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbalockacba"), "UTF-8");
        data += "&" + URLEncoder.encode("ticket", "UTF-8") + "=" + URLEncoder.encode(ticket, "UTF-8");
        data += "&" + URLEncoder.encode("delete", "UTF-8") + "=" + URLEncoder.encode("true", "UTF-8");

        return data;
    }

    @Override
        protected void onPostExecute(String result) {
                if(INSERT){
                    try {

                       JSONObject obj = new JSONObject(result);
                       MainActivity.ticket_number=obj.getInt("ticket");//saves the potential
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(DELETE){//delete the remebred ticket
                    MainActivity.ticket_number=-1;
                    MainActivity.TICKET=null;
                }else{//TICKET was a success
                    MainActivity.TICKET= new Ticket(store_id,ticket,name,stylist,phone);
                }
        }


    }


package app.reservation.acbasoftare.com.reservation.WebTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.stylist_bitmaps;

/**
 * Created by user on 2016-08-06.
 */
public class StylistWebTask extends AsyncTask<String, Void, String> {
    private ArrayList<Stylist> stylist_ticket_list;
    private ProgressBar progressBar;
    private HashMap<String, Stylist> stylist_hash;
    private FirebaseStore store;
    private View rootView;

    private final String link = "http://acbasoftware.com/pos/stylist.php";
    private ListView lvv;
    private MainActivity ma;
    public StylistWebTask(MainActivity ma, View rv) {
        this.ma = ma;
        stylist_ticket_list = new ArrayList<Stylist>();
        stylist_hash = new HashMap<String, Stylist>();
        store = ma.store_list.get(ma.selectedPosition);
        //  final LayoutInflater factory = MainActivity.a.getLayoutInflater();
        rootView= rv;//factory.inflate(R.layout.fragment_layout_live_feed, null);

    }

    protected void onPreExecute() {
        showProgressBar(true,rootView);
    }

    private void showProgressBar(boolean show, View rootView) {
       /* progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_live_feed);
        if (show == false) {
            progressBar.setIndeterminate(false);
            progressBar.setVisibility(View.GONE);
        }else {
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    protected String doInBackground(String... arg0) {

        try {
            String store_id = arg0[0];///is the same as phone....phone is id for db
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String data = URLEncoder.encode("stylist", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastylistlistacba"), "UTF-8");
            data += "&" + URLEncoder.encode("store_id", "UTF-8") + "=" + URLEncoder.encode(store_id, "UTF-8");
            data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(sdf.format(date), "UTF-8");
            data += "&" + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(store.getPhone(), "UTF-8");

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
            JSONArray jArray = jObject.getJSONArray("stylist");///get tickets and id
            JSONArray jArray2 = jObject.getJSONArray("stylist_names");//get general info of stys
            if(jArray.length() ==0 && jArray2.length()==0){
                ListView lv = (ListView)rootView.findViewById(R.id.fragment_livefeed_listview);
                ListAdapter la = new ListViewAdpaterStylist(rootView.getContext(), R.layout.list_view_live_feed, stylist_ticket_list);
                lv.setAdapter(la);
                return;
            }

            for (int i = 0; i < jArray2.length(); i++) {
                try {
                    JSONObject jobj = jArray2.getJSONObject(i);
                    // Pulling items from the array
                    String fname = jobj.getString("fname");
                    String mname = jobj.getString("mname");
                    String lname = jobj.getString("lname");
                    String stylist_id = jobj.getString("stylist_id");
                    boolean available = jobj.getString("available").contains("1");//check to confirm 0 is false and 1 is true
                    String qrimage = jobj.getString("image");

                  //  byte[] pic = Base64.decode(qrimage.getBytes(), Base64.DEFAULT);

                   // Bitmap bmp = BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length);//Utils.resize(BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length),100,100);
                    String phone = jobj.getString("phone");
                    // Bitmap myBitmap = jobj.getby

                    Stylist s = new Stylist(stylist_id,fname,mname,lname,available,qrimage,phone,store.getPhone());
                    this.stylist_hash.put(stylist_id, s);

                } catch (JSONException e) {
                    displayErr();
                    e.printStackTrace();
                }
            }
            // Stylist store = new Stylist();
            //this.stylist_hash.put(store.getID(),store);////add store as a stylist

            for (int i = 0; i < jArray.length(); ++i) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    int wait = oneObject.getInt("wait");
                    String stylist_id = oneObject.getString("stylist_id");
                    Stylist s = this.stylist_hash.get(stylist_id);
                    s.setWait(wait);
                    this.stylist_hash.put(s.getId(),s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e1) {
            displayErr();
            e1.printStackTrace();
            return;
        }

        /**  for(String id : this.stylist_hash.keySet()){
         Stylist s = this.stylist_hash.get(id);
         this.stylist_ticket_list.add(s);
         }*/
        this.stylist_ticket_list = new ArrayList<Stylist>(this.stylist_hash.values());
        // Sorting by id...thus the store will always be set first to show
        Collections.sort(this.stylist_ticket_list, new Comparator<Stylist>() {
            @Override
            public int compare(Stylist s, Stylist ss)
            {
                return  s.getId().compareTo(ss.getId());
            }
        });
        showProgressBar(false, this.rootView);

        ma.stylists_list=this.stylist_ticket_list;
        //store.setStylistHashMap(this.stylist_hash);
        ma.store_list.remove(ma.selectedPosition);///remove
        ma.store_list.add(ma.selectedPosition,store);//hopefully overloads the object
       initListView();
       // rootView.setVisibility(View.VISIBLE);
       // ma.noStaff=false;
        //MainActivity.rootView_Reservation.setVisibility(View.VISIBLE);
    }

    private void initListView() {
        if(ma.stylists_list==null){
            lvv=(ListView) rootView.findViewById(R.id.fragment_livefeed_listview);
            lvv.setAdapter(null);
        }else {
            ListAdapter la=new ListViewAdpaterStylist(rootView.getContext(), R.layout.list_view_live_feed, ma.stylists_list);
            lvv=(ListView) rootView.findViewById(R.id.fragment_livefeed_listview);
            lvv.setAdapter(la);
        }
    }

    private void displayErr() {
        showProgressBar(false,rootView);

        ma.stylists_list=null;
        initListView();
        //rootView.setVisibility(View.GONE);
        // MainActivity.rootView_Reservation.setVisibility(View.GONE);
       // ma.updateTab3();
        //ma.noStaff=true;
    }

    public class ListViewAdpaterStylist extends ArrayAdapter<Stylist> {
        public ListViewAdpaterStylist(Context c, int list_view_live_feed, ArrayList<Stylist> values) {
            super(c, list_view_live_feed, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            //View rowView = inflater.inflate(R.layout.list_view_live_feed, parent, false);
            Stylist s = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_live_feed, parent, false);

            RadioButton r = (RadioButton) convertView.findViewById(R.id.live_feed_radiobtn);
            //r.setText("Stylist: " + s.getName() + "\n" + "Waiting: " + s.getWait()+"\nApprox. Wait: "+Utils.calculateWait(s.getWait())+"\nEstimated Time: "+ Utils.calculateTimeReady(s.getWait()));
            r.setChecked(position ==  ma.stylist_position);
            r.setTag(position);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ma.stylist_position = (Integer) view.getTag();
                    notifyDataSetChanged();
                    //Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                }
            });
            TextView tv_stylist=(TextView)convertView.findViewById(R.id.textView_stylist_lv);
            tv_stylist.setText(s.getName().toUpperCase());
            //setListener(tv_stylist,r);
            TextView tv_waiting=(TextView)convertView.findViewById(R.id.tv_waiting_lv);
            tv_waiting.setText(""+s.getWait());
            //setListener(tv_waiting,r);
            TextView tv_approx_wait=(TextView)convertView.findViewById(R.id.textView_aprox_wait_lv);
            tv_approx_wait.setText(Utils.calculateWait(s.getWait()));
            //setListener(tv_approx_wait,r);
            TextView tv_readyby=(TextView)convertView.findViewById(R.id.textView_readyby_lv);
            tv_readyby.setText(Utils.calculateTimeReady(s.getWait()));
            //setListener(tv_readyby,r);
            ///////
            TextView tv3=(TextView)convertView.findViewById(R.id.textView3);
            //setListener(tv3,r);
            TextView tv4=(TextView)convertView.findViewById(R.id.textView4);
            //setListener(tv4,r);
            TextView tv5=(TextView)convertView.findViewById(R.id.textView5);
           // setListener(tv5,r);
            TextView tv6=(TextView)convertView.findViewById(R.id.textView6);
           // setListener(tv6,r);
            //Bitmap myBitmap = BitmapFactory.decodeFile("\\res\\drawable\\logo.png");
            QuickContactBadge iv = (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge);

            if(stylist_bitmaps!=null && stylist_bitmaps.size()>= position+1) {
                iv.setImageBitmap(stylist_bitmaps.get(position));//Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes())));
               if(position==0) {
                   iv.assignContactFromPhone(ma.store_list.get(ma.selectedPosition).getPhone(), true);
               }else{
                   iv.assignContactFromPhone(s.getPhone(),true);
               }
            }

            iv.setMode(ContactsContract.QuickContact.MODE_LARGE);
            // iv.setVisibility(View.VISIBLE);
                /*r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPosition = (Integer) view.getTag();
                        notifyDataSetChanged();
                        Toast.makeText(getContext(), getItem(position) + " selected", Toast.LENGTH_LONG).show();
                    }
                });*/
            return convertView;
        }
        /*private void setListener(TextView tv, final RadioButton rb){
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ma.stylist_position = (Integer) rb.getTag();
                    rb.setChecked(true);
                    notifyDataSetChanged();

                }
            });
        }*/

    }
}

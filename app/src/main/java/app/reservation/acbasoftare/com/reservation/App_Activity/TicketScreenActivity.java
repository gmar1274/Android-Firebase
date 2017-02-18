package app.reservation.acbasoftare.com.reservation.App_Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.Inflater;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStoreMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.FirebaseWebTasks.FirebaseWebTasks;
import app.reservation.acbasoftare.com.reservation.ListAdapters.ListViewAdapterFirebaseTicket;
import app.reservation.acbasoftare.com.reservation.ListAdapters.ListViewAdpaterStylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;



/**
 * Main display for customer. Self kiosk serve
 */
public class TicketScreenActivity extends AppCompatActivity {
    public static final String STYLIST_FIREBASE_URL="images/stylists/";
    private WebService ws;
    private String TAG="FIREBASE TAG::";
   // public  StorageReference mStorageRef;
    //public  FirebaseDatabase database;
    private String email;
    private String password;
    public ArrayList<Stylist> stylist_list;
    // public static String store_id=null;
    public FirebaseStore store;
    public  int stylist_position=-1;
    //public DatabaseReference myRef;
    public ListView listView;
    // public ArrayList<Ticket> tickets;
    public static  HashMap<String , Bitmap> bitmaps;
    public HashMap<String,Stylist> sty_hm;
    public long current_ticket;

    //public Store store_firebase;
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // outState.putString("store_id", TicketScreenActivity.store_id);
        outState.putParcelable("store_firebase",store);
        outState.putParcelableArrayList("stylist_list", stylist_list);
        outState.putString("email", email);
        outState.putString("password", password);
        outState.putInt("stylist_position", this.stylist_position);
        // outState.putParcelableArrayList("tickets",tickets);
        outState.putSerializable("stylist_bitmaps",bitmaps);
        outState.putSerializable("sty_hm",sty_hm);
        outState.putLong("current_ticket", current_ticket);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_screen);

        if(savedInstanceState != null) {
            //store_id=savedInstanceState.getString("store_id");
            store = savedInstanceState.getParcelable("store_firebase");
            stylist_list=savedInstanceState.getParcelableArrayList("stylist_list");
            email=savedInstanceState.getString("email");
            password=savedInstanceState.getString("password");
            stylist_position=savedInstanceState.getInt("stylist_position");
            // tickets = savedInstanceState.getParcelableArrayList("tickets");
            bitmaps = (HashMap<String, Bitmap>) savedInstanceState.getSerializable("stylist_bitmaps");
            sty_hm = (HashMap<String, Stylist>) savedInstanceState.getSerializable("sty_hm");
            this.current_ticket = savedInstanceState.getLong("current_ticket");
        } else {
            init();
        }


        Button ticket=(Button) this.findViewById(R.id.ticketBTN);
        ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reserve();
            }
        });

    }

    /**
     * Initialize all variables
     */
    private void init() {
        sty_hm = new HashMap<>();
        bitmaps = new HashMap<>();
        // tickets = new ArrayList<>();
        listView = (ListView)this.findViewById(R.id.listview_main_ticket_queue);
        ArrayAdapter<Ticket> adpt = new ArrayAdapter<Ticket>(this,R.layout.array_adapter_layout,R.id.textView_arrayadapter,new ArrayList<Ticket>());
        this.listView.setAdapter(adpt);
        store=null;
        stylist_list=new ArrayList<>();
        email=this.getIntent().getStringExtra("email");
        password=this.getIntent().getStringExtra("password");
        final ProgressDialog pd=ProgressDialog.show(this, "Initializing software.", "Loading...", true, false);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                pd.show();
            }
        }).start();*/
        this.stylist_position=-1;///none selected
        //database=FirebaseDatabase.getInstance();
        //mStorageRef= FirebaseStorage.getInstance().getReference();
        /// myRef=database.getReference();//.getReference("message");
        /***********DEBUG POPULATE STORES FROM TEST DB MYSQL acba.com*************/
        //StoresWebTaskPopulateStoreFromOldMYSQLServer swt = new StoresWebTaskPopulateStoreFromOldMYSQLServer(email,password,pd);
        //swt.execute();

        FirebaseLoadStylist l = new FirebaseLoadStylist(this,pd,email,password);
        l.execute();

        //ClientWebTask cwt=new ClientWebTask(this, email, password, pd);
        //cwt.execute();


    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    public void onBackPressed() {
        Log.d("Back Press in Ticket", "pressed");
    }

    /**
     * On Button click to grab ticket...
     * Pass this object to class
     */
    private void reserve() {
        Intent i=new Intent(this, InStoreTicketReservationActivity.class);
        this.stylist_position=-1;//reset every time
        ///FirebaseIntent fi = new FirebaseIntent(stylist_list,tickets,stylist_position);

        Bundle b = new Bundle();
        b.putSerializable("sty_hm",sty_hm);
        b.putParcelableArrayList("stylist_list",this.stylist_list);
        b.putParcelableArrayList("tickets",this.stylist_list);
        b.putParcelable("store",store);
        b.putInt("stylist_position",this.stylist_position);
        b.putLong("current_ticket",current_ticket);
        i.putExtras(b);
        startActivity(i);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(resultCode==1 && requestCode==1){//1 will be the default readBack value from InStoreTicketReservationActivity
            Log.d("onActivityResult TAG","on activity result tag completed in ticketScreenAct");
            // this.stylist_list = intent.getParcelableArrayListExtra("stylist_list");
            Bundle b = intent.getExtras();
            // this.tickets = b.getParcelableArrayList("tickets");
            // this.store = intent.getParcelableExtra("store");
            //this.stylist_position =intent.getIntExtra("stylist_position",-1);
            this.stylist_list = b.getParcelableArrayList("stylist_list");
            this.sty_hm = (HashMap<String, Stylist>) b.getSerializable("sty_hm");
            this.store = b.getParcelable("store");
            this.current_ticket = b.getLong("current_ticket");
            super.onActivityResult(requestCode,resultCode,intent);

        }
    }
    private void showCreditCard() {
        CreditCardDialog ccd=new CreditCardDialog(this, ws);
        ccd.showCreditCardDialog(true);
    }

   /* public void updateListView(List<Ticket> tickets) {
        if(stylist_list.size()==0){
            return;
        }
        for(Ticket t : tickets){//linear runtime
            Stylist sty = sty_hm.get(t.getStylist_id());//get the stylist of ticket
            if(sty != null) {
                sty.setWait(Integer.valueOf(t.getTicket_number()));//assume the last known record is the current max
                sty_hm.put(sty.getId(),sty);//update hashmap
                int pos = stylist_list.indexOf(sty);//old to new
                stylist_list.remove(pos);
                stylist_list.add(pos, sty);//update stylist_list
            }
        }//end for
        listView.setAdapter(null);
        ArrayAdapter<Ticket> adpt = new ArrayAdapter<Ticket>(this,R.layout.array_adapter_layout,R.id.textView_arrayadapter,tickets);
        listView.setAdapter(adpt);
        //((ArrayAdapter<Ticket>)listView.getAdapter()).notifyDataSetChanged();
        Log.d("ARRAY ADAPTER updated","notifyDataSetChanged");
    }*/

    public void LoadStylistsAfterStorePick(final ProgressDialog pd) {

        //final ProgressDialog pd = ProgressDialog.show(this,"Initializing Software","Please Wait...",true,false);
        pd.show();
        Log.e("Loading after store","loading after store pick...");
        DatabaseReference ref_get_hm = FirebaseDatabase.getInstance().getReference().child("stylists/"+String.valueOf(this.store.getStore_number()));
        ref_get_hm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null)return;
                GenericTypeIndicator<Map<String,Stylist>> gti = new GenericTypeIndicator<Map<String, Stylist>>() {};
                Map<String,Stylist> hm = dataSnapshot.getValue(gti);
                Log.e("FETCHING STYLISTs: ",hm.toString());
                sty_hm = new HashMap<String, Stylist>(hm);
                stylist_list = new ArrayList<Stylist>(hm.values());
                Collections.sort(stylist_list);
                FirebaseWebTasks.downloadImages(TicketScreenActivity.this,TicketScreenActivity.this.store, TicketScreenActivity.this.stylist_list, pd);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(pd != null)pd.dismiss();
                Log.e("Error on fetching stya","err..."); //databaseError.toException().printStackTrace();
            }
        });


    }

    /**
     * Will performa an algorithm that naively distributes the no_preference ticket Queue and assigns to every
     * stylist one by one until the queue runs out. Doing this pproach we can then get the index of the position of the potential customers ticket
     * and pretend a more or less approximation to their wait for each stylist...
     */
    private void predictTicketWait(List<Ticket> list) {
        final Ticket user_t = new Ticket(list.get(list.size()-1));
        zeroOutStylistWait();//wait is Zeroed out...
        HashMap<String,ArrayList<Ticket>> queues = createQueues(list,user_t);//spliceTicketsFor("-1",list);
        ArrayList<Ticket> no_pref = queues.get("-1");//get the store no pref waiting
        queues.remove("-1");//we dont need that no pref queue no more...
        while (no_pref.size()>0){///infinite because i didnt remove index 0...
            final Ticket no_pref_t = no_pref.remove(0);//because its orederd low to high

            HashMap<String,ArrayList<Ticket>> lowest_stylist_queue = distributeLoad(queues);//return the lowest queue of all stylist
            String id = (String) lowest_stylist_queue.keySet().toArray()[0];//get the key
            ArrayList<Ticket> sl = lowest_stylist_queue.get(id);
            if(!sl.contains(no_pref_t))
            {
                sl.add(no_pref_t);
            }
            Collections.sort(sl);
            queues.put(id,sl);//update new queue
            Log.e("loop::","loop");
        }//done balancing no pref tickets...
        int lowest = Integer.MAX_VALUE;
        for(String id : queues.keySet()){

            Stylist s = sty_hm.get(id);//got the stylsit...
            ArrayList<Ticket> sl = queues.get(id);//get the queue of the stylist
            Log.e("QUEUES: ","id: "+id+" Q:: "+sl+"\n");
            int pos = sl.indexOf(user_t);
            if(pos<lowest){
                lowest = pos;//no pref lowest time
            }
            s.setPsuedo_wait(pos);
            sty_hm.put(id,s);
        }
        Stylist no = sty_hm.get("-1");
        no.setPsuedo_wait(lowest);
        sty_hm.put("-1",no);
        updateStylistListViewAndTicketQueue(sty_hm,list);
    }

    private void updateStylistListViewAndTicketQueue(HashMap<String, Stylist> sty_hm, List<Ticket> tickets) {
        ListView lv = (ListView)this.findViewById(R.id.listview_main_ticket_queue);
        ListViewAdapterFirebaseTicket la = new ListViewAdapterFirebaseTicket(TicketScreenActivity.this,0,tickets);
        lv.setAdapter(la);

        ArrayList<Stylist> list = new ArrayList<>(this.sty_hm.values());
        Collections.sort(list);
        this.stylist_list = list;


        // ListViewAdpaterStylist la = new ListViewAdpaterStylist(this,0,stylist_list,bitmaps);
        //lv.setAdapter(la);
    }

    private HashMap<String,ArrayList<Ticket>> distributeLoad(HashMap<String, ArrayList<Ticket>> queues) {
        HashMap<Long,String> lowest = new HashMap<>();
        long l = Long.MAX_VALUE;
        lowest.put(Long.MAX_VALUE,"-1");
        for(String key : queues.keySet()){
            ArrayList<Ticket> sublist = queues.get(key);
            long size = sublist.size();
            if(size < l){
                l = size;
                lowest.clear();
                lowest.put(l,key);//save the lowest number and the stylist_id (key) queue
            }

        }//finshed go the lowest..
        HashMap<String,ArrayList<Ticket>> t = new HashMap<>();
        String id = lowest.get(l);
        ArrayList<Ticket> sub_list = queues.get(id);
        Collections.sort(sub_list);
        t.put(id, sub_list);
        return t;
    }

    /**
     * //Return a ordered sublist that contains all elemts of ID sty_id..
     * Return a hashMap
     * @param list
     * @return
     */
    private HashMap<String,ArrayList<Ticket>> createQueues(List<Ticket> list, Ticket user){//ArrayList<Ticket> spliceTicketsFor(String sty_id, List<Ticket> list) {
       HashMap<String,ArrayList<Ticket>> queues = new HashMap<>();
        for(Stylist s : this.stylist_list){//init queue for each stylist with the potential ticket
            ArrayList<Ticket> sublist = new ArrayList<>();
            sublist.add(user);
            queues.put(s.getId(),sublist);
        }
        for(Ticket t : list){
            Stylist s = sty_hm.get(t.stylist_id);
            s.incrementWait();
            sty_hm.put(s.getId(), s);
           ArrayList<Ticket> sublist = queues.get(t.stylist_id);
            sublist.add(t);
            Collections.sort(sublist);
            queues.put(t.stylist_id,sublist);//update queue with a ordered wait by unique id priority..
        }
        return queues;
    }

    /**
     * Zeros out both hashmap and stylist ArrayList...
     */
    private void zeroOutStylistWait() {
        for(Stylist s : this.stylist_list){
            s.setWait(0);
            s.setPsuedo_wait(0);
            this.sty_hm.put(s.getId(),s);
        }
        this.stylist_list = new ArrayList<>(this.sty_hm.values());
        Collections.sort(this.stylist_list);
    }

    public void loadTckets(final ProgressDialog pd) {
        DatabaseReference db_t = FirebaseDatabase.getInstance().getReference().child("tickets/"+store.getStore_number());//refernce to all tickets for the store_firebase Store.
        // Read from the database
        db_t.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot == null || dataSnapshot.getValue()==null){return;}
               // Log.d("FIREBASE LISTENER", "Value UPDATEDDDDD...."+dataSnapshot.getValue().toString());
                GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                List<Ticket> current_tickets  = dataSnapshot.getValue(gti);
                if(current_tickets.size()==0 || stylist_list==null || stylist_list.size()==0)return;


                //update current store_firebase absolute ticket #
                //update stores current ticket list but as of now im not storing that just displaying
                //act.tickets = new ArrayList<Ticket>(current_tickets);//finalize the updating
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                predictTicketWait(current_tickets);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                if(pd != null)pd.dismiss();
                // Failed to read value
                Log.w("FIREBASE ERROR", "Failed to read value.", error.toException());
            }
        });
    }
}

class ClientWebTask extends AsyncTask<String, Void, String> {
    final String link="http://www.acbasoftware.com/pos/storeInfoByUsername.php";
    private String email, password;
    private ProgressDialog pd;
    private Activity activity;

    public ClientWebTask(Activity a, String email, String pass, ProgressDialog pd) {
        this.activity=a;
        this.email=email;
        this.password=pass;
        this.pd=pd;
    }

    @Override
    protected void onPreExecute() {
        if(pd != null && !pd.isShowing()) this.pd.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            //send phone as email or update the user with

            String data=URLEncoder.encode("store_login", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastore_loginacba"), "UTF-8");
            data+="&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data+="&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
                //Log.e("WEB RESPONSE UN:: ",line);
                this.decodeJSON(line);//this will spit out a JSON OBJECT named Stylist
                //break;
            }
            return sb.toString();
        } catch(Exception e) {
            this.pd.dismiss();
            return new String("Exception: " + e.getMessage());////no store_firebase id...
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if(result == null) {
            if(pd != null) pd.dismiss();
            return;
        }
        // Log.d("RESULT FROM WEB:: ",result);
        try {
            /// TicketScreenActivity.myRef.child(store_id+"/stylists/").push().setValue(TicketScreenActivity.stylist_list);
            //StorageReference sr=TicketScreenActivity.mStorageRef.child(store_id + "/" + STYLIST_FIREBASE_URL);

            /* for(final Stylist s : TicketScreenActivity.stylist_list) {
             final File localFile=File.createTempFile(s.getID(), "png");
             sr.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
            Bitmap bitmap=BitmapFactory.decodeFile(localFile.getAbsolutePath());
            // mImageView.setImageBitmap(bitmap);
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull Exception exception) {
            FirebaseWebTasks.uploadImage(s.getImage(), s.getID());
            }
            });
             }
*/

            // for(Stylist s : TicketScreenActivity.stylist_list) {FirebaseWebTasks.uploadImage(s.getImage(), s.getID());}

            if(pd != null) pd.dismiss();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void decodeJSON(String json) {
        try {

            JSONObject jobj=new JSONObject(json);
            //  Log.e("UNPARSED::",json);
            // Log.e("PARSED::",jobj.toString());
            // Pulling items from the array
            String fname=jobj.getString("fname");
            String mname=jobj.getString("mname");
            String lname=jobj.getString("lname");
            String stylist_id=jobj.getString("stylist_id");
            boolean available=jobj.getString("available").contains("1");//check to confirm 0 is false and 1 is true
            String qrimage=jobj.getString("image");

            // byte[] qrimageBytes= Base64.decode(qrimage.getBytes(), Base64.DEFAULT);

            //Bitmap bmp=BitmapFactory.decodeByteArray(qrimageBytes, 0, qrimageBytes.length);//Utils.resize(BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length),100,100);
            String store_id=jobj.getString("store_id");
            // Bitmap myBitmap = jobj.getby

            Stylist s=new Stylist(stylist_id, fname, mname, lname, available, qrimage, null, store_id);
            // if(TicketScreenActivity.store_id == null) { //save the store_id...
            //    TicketScreenActivity.store_id=store_id;
            //}
            // stylist_list.add(s);//add stylist
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class StoresWebTaskPopulateStoreFromOldMYSQLServer extends AsyncTask<String, Void, String> {

    private ProgressDialog pd;
    private String email,password;
    public StoresWebTaskPopulateStoreFromOldMYSQLServer(String email,String pass,ProgressDialog pd) {
        this.pd = pd;
        this.email=email;
        this.password=pass;
    }

    protected void onPreExecute() {

    }
    @Override
    protected String doInBackground(String... arg0) {

        try {
            int miles = 200;

            String link = "http://acbasoftware.com/pos/store.php";
            String data = URLEncoder.encode("store", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastorelistacba"), "UTF-8");

            data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(34.0633 + "", "UTF-8");
            data += "&" + URLEncoder.encode("lon", "UTF-8") + "=" + URLEncoder.encode(  -117.6509+ "", "UTF-8");

            /**data += "&" + URLEncoder.encode("radius", "UTF-8") + "=" + URLEncoder.encode(miles + "", "UTF-8");//meters
             */
            // String data=URLEncoder.encode("store_login", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastore_loginacba"), "UTF-8");
            data+="&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
            data+="&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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

        Log.e("Web SERVER RESPONSE::",result);
        Map<String,Object> store_map = new HashMap<>();
        //  ArrayList<Store> store_firebase = new ArrayList<>();

        HashMap<String,Stylist> hm = new HashMap<>();

        Map<String,Object>sty_hm=new HashMap<>();////firebase/stylists/stylistHashMap{hm}
        Map<String, Object> firebaseStore = new HashMap<>();

        Map<String,Object>firebaseMetaData = new HashMap<>();
        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray jArray2 = jObject.getJSONArray("stylist");
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
                    String phone = jobj.getString("phone");
                    // Bitmap myBitmap = jobj.getby

                    Stylist s = new Stylist(stylist_id,fname,mname,lname,available,null,phone,null);

                    hm.put(s.getId(),s);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            JSONArray jArray = jObject.getJSONArray("store");
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    double lat = oneObject.getDouble("lat");
                    double lon = oneObject.getDouble("lon");
                    LatLng loc2 = new LatLng(lat, lon);//meters

                    String name = oneObject.getString("Store");
                    String addr = oneObject.getString("Address");
                    String citystate = oneObject.getString("CityState");

                    String phone = oneObject.getString("phone");
                    //  double miles_away = oneObject.getDouble("distance");
                    double ticket_price = oneObject.getDouble("ticket_price");
                    String open = oneObject.getString("open_time");
                    String close = oneObject.getString("close_time");
                    double cprice = oneObject.getDouble("reservation_price");
                    long current_ticket = oneObject.getLong("current_ticket");
                    String card_id = oneObject.getString("card_id");//stripe
                    String subscription_id = oneObject.getString("subscription_id");
                    String email = oneObject.getString("email");
                    String password = oneObject.getString("password");
                    String google = oneObject.getString("google_place_id");
                    Store s = new Store(i,name, addr, citystate, phone, lat, lon,i,i,ticket_price,open,close,cprice,email,password,current_ticket,card_id,subscription_id);
                    //store_firebase.add(s);
                    s.setStylistHashMap(hm);
                    s.addStoreStylist();
                    hm.put("-1",s.getStylistHashMap().get("-1"));
                    store_map.put(""+i,s);

                    sty_hm.put(i+"",hm);

                    FirebaseStoreMetaData fsmd = new FirebaseStoreMetaData(i,s.getPhone(),loc2,google);
                    firebaseMetaData.put(i+"",fsmd);
                    FirebaseStore fs = new FirebaseStore(i,name, addr, citystate, phone, loc2,ticket_price,open,close,cprice,email,password,card_id,subscription_id,google);
                    firebaseStore.put(i+"",fs);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }///////end for
            // Map<String, Object> hashtaghMap = new ObjectMapper().convertValue(hashtaghModel, Map.class);
            //update just make unstatic   // myRef.updateChildren(store_map); //push cretes a new timestamp in the directory every time i call it
            Log.e("MAKING CALLLLLL:","making calls");
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("user-meta-data").setValue(firebaseMetaData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("meta data: ",""+task.isSuccessful());
                }
            });
            ref.child("user").setValue(firebaseStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("user: ",""+task.isSuccessful());
                }
            });
            List<Ticket> t = new ArrayList<>();
            Map<String,Object> mt = new HashMap<>();
            for(int i =0 ; i < firebaseMetaData.values().size(); ++i){
                mt.put(i+"",t);
            }
            ref.child("tickets").setValue(mt).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("tickets: ",""+task.isSuccessful());
                }
            });
            ref.child("stylists").setValue(sty_hm).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.e("stylists: ",""+task.isSuccessful());
                }
            });
            Log.e("HERe","here");


        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(pd.isShowing() && pd!=null){
                this.pd.dismiss();
            }
        }
    }
}
/**
 * THIS CLASS IS REALLY IMPORTANT.
 * I use this task to download the current STORE and all the Stylists picutueres to populate the the MAIN SOFTWARE...
 */
class FirebaseLoadStylist extends AsyncTask<String,Void, Void> {
    private ProgressDialog pd;
    private String email, password;
    private FirebaseStore store_firebase;
    private TicketScreenActivity act;

    public FirebaseLoadStylist(TicketScreenActivity act,ProgressDialog pd, String email, String pass) {
        this.email = email;
        this.password = pass;
        this.pd = pd;
        this.store_firebase = null;
        this.act = act;
    }

    @Override
    protected void onPreExecute() {
        if (pd != null && !pd.isShowing()) {
            this.pd.show();
        }else{
            ///
        }
    }


    @Override
    protected Void doInBackground(final String... strings) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        //roots order by child get the Store JSON and gets the value email
        reference.orderByChild("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot is the "issue" node with all children with id 0
                boolean found = false;
                List<FirebaseStore> list = new ArrayList<FirebaseStore>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {//will produce all of the keys in firebase db
                    // Log.e("IN LOOP FIREBASE:: ",""+dds.getKey());
                    // do something with the individual "key"
                    FirebaseStore s = ds.getValue(FirebaseStore.class);
                    if (s.getEmail().equals(email) && s.getPassword().equals(password)) {
                        //   Log.e("Store selected is: ", s.getName());
                        //Log.e("STORE KEY",ds.getKey());// GenericTypeIndicator<List<Stylist>> g = new GenericTypeIndicator<List<Stylist>>() {};
                        //Log.e("Val:",ds.getValue().toString()); //List<Stylist> list = ds.getValue(g);
                        // List<Stylist> list = getArrayListFromSnapshot(ds);// ds.child("stylistArrayList").getValue(g);//this works because of the structure of firebase...url etc...

                        // Object sty_obj = ds.child("stylistArrayList").get;
                        // Object obj = ds.child("stylistArrayList").getValue();
                        //  Log.d("Stylists class:: ", sty_obj.toString());
                        //Log.e("Stylist are without/ge", obj.toString());
                        store_firebase = s;
                        list.add(store_firebase);
                        // store_firebase.setStylistList(list);
                        //  store_firebase.setStylistList(l);
                        found = true;
                       // break;
                    }

                }
                if (found) {///set up program like like to database
                    ///Display which sdtore this is to the user...

                   displayStoreChooser(list,act,pd);

                }else {
                    pd.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERRRRRRR:", databaseError.getMessage());
            }
        });

        /** Query query = reference.child("issue").orderByChild("id").equalTo(0);
         query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
        // dataSnapshot is the "issue" node with all children with id 0
        for (DataSnapshot issue : dataSnapshot.getChildren()) {
        // do something with the individual "issues"
        }
        }
        }
        @Override public void onCancelled(DatabaseError databaseError) {
        }
        });*/ //QUERY by the directory/JSON : {id:}
        return null;
    }

    private void displayStoreChooser(List<FirebaseStore> list, final TicketScreenActivity act, final ProgressDialog pd) {
        pd.dismiss();
        AlertDialog.Builder b = new AlertDialog.Builder(this.act);
        b.setTitle("Select your shop.");
        LayoutInflater i = this.act.getLayoutInflater();
        View view = i.inflate(R.layout.alert_dialog_choose_store,null);
        final ListView lv = (ListView) view.findViewById(R.id.listview_store_alertdialog);
        ListViewAdapterFirebaseStore la = new ListViewAdapterFirebaseStore(this.act,list);
        lv.setAdapter(la);
        b.setCancelable(false);
        b.setView(view);
        b.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListViewAdapterFirebaseStore la = (ListViewAdapterFirebaseStore) lv.getAdapter();
                act.store = la.getCurrentSelectedStore();
                TextView tv = (TextView) act.findViewById(R.id.store_name_tv);
                tv.setText(act.store.getName().toUpperCase());
               //pd.show();
              continueWithLoading();

            }
        });
         final AlertDialog alert = b.create();
        alert.show();

    }

    private void continueWithLoading() {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                act.LoadStylistsAfterStorePick(pd);
            }
        });

    }

    @Override
    protected void onPostExecute(Void result) {
        ///all handled in the thread doInBackground..



    }

    /**
     * This is for Store list.
     * ArrayAdpater for stores.
     */
    public static class ListViewAdapterFirebaseStore extends ArrayAdapter<FirebaseStore> {
        private Activity ma;
        private int selectedIndex = -1;
        public ListViewAdapterFirebaseStore(Activity ma, List<FirebaseStore> values) {
            super(ma, R.layout.list_view_layout, values);
            this.ma = ma;
        }

        /**
         * Implement getView method for customizing row of list view.
         * this method creates store_list single view that correponds to the data being passed in.
         * get the STORE data by getItem(position)
         */
        public View getView(final int position_item, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            final FirebaseStore store = getItem(position_item);
            View rowView = inflater.inflate(R.layout.list_view_layout, parent, false);
            RadioButton r = (RadioButton) rowView.findViewById(R.id.radio_button);

            r.setText("Shop: " + store.getName() + "\nStore ID: "+store.getStore_number()+"\nPhone: "+store.getPhone());//+"\n" + "Address: " + store.getAddress().toUpperCase() + "\n" + store.getCitystate().toUpperCase());

            r.setChecked(position_item == selectedIndex);
            r.setTag(position_item);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    selectedIndex = (Integer) view.getTag();
                    notifyDataSetChanged();//updates the button click isset

                }
            });
            return rowView;
        }
        public FirebaseStore getCurrentSelectedStore(){
            return  this.getItem(this.selectedIndex);
        }
    }


}
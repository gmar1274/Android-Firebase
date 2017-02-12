package app.reservation.acbasoftare.com.reservation.App_Activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.FirebaseWebTasks.FirebaseWebTasks;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;



/**
 * Main display for customer. Self kiosk serve
 */
public class TicketScreenActivity extends AppCompatActivity {
    public static final String STYLIST_FIREBASE_URL="images/stylists/";
    private WebService ws;
    private String TAG="FIREBASE TAG::";
    public  StorageReference mStorageRef;
    public  FirebaseDatabase database;
    private String email;
    private String password;
    public static  ArrayList<Stylist> stylist_list;
   // public static String store_id=null;
    public static  Store store;
    public  int stylist_position=0;
    public static DatabaseReference myRef;
    public ListView listView;
    public static ArrayList<Ticket> tickets;
    public static  ArrayList<Bitmap>bitmaps;

    //public Store store_firebase;
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putString("store_id", TicketScreenActivity.store_id);
        outState.putParcelable("store_firebase",store);
        outState.putParcelableArrayList("stylist_list", stylist_list);
        outState.putString("email", email);
        outState.putString("password", password);
        outState.putInt("stylist_position", this.stylist_position);
        outState.putParcelableArrayList("tickets",tickets);
        outState.putParcelableArrayList("stylist_bitmaps",bitmaps);
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
            tickets = savedInstanceState.getParcelableArrayList("tickets");
           bitmaps = savedInstanceState.getParcelableArrayList("stylist_bitmaps");
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
        bitmaps = new ArrayList<>();
        tickets = new ArrayList<>();
        listView = (ListView)this.findViewById(R.id.listview_main_ticket_queue);
        ArrayAdapter<Ticket> adpt = new ArrayAdapter<Ticket>(this,R.layout.array_adapter_layout,R.id.textView_arrayadapter,tickets);
        this.listView.setAdapter(adpt);
        store=null;
        stylist_list=new ArrayList<>();
        email=this.getIntent().getStringExtra("email");
        password=this.getIntent().getStringExtra("password");
        final ProgressDialog pd=ProgressDialog.show(this, "Initializing software.", "Loading...", true, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                pd.show();
            }
        }).start();
        this.stylist_position=-1;///none selected
        database=FirebaseDatabase.getInstance();
        mStorageRef= FirebaseStorage.getInstance().getReference();
      /// myRef=database.getReference();//.getReference("message");
        /***********DEBUG POPULATE STORES FROM TEST DB MYSQL acba.com*************/
       // StoresWebTaskPopulateStoreFromOldMYSQLServer swt = new StoresWebTaskPopulateStoreFromOldMYSQLServer(email,password,pd);
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
        //i.putParcelableArrayListExtra("stylist_list",this.stylist_list);
        //i.putParcelableArrayListExtra("tickets",this.stylist_list);
        //i.putExtra("store",store);
        i.putExtra("stylist_position",this.stylist_position);

        this.startActivity(i);
    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    if(resultCode==1){//1 will be the default readBack value from InStoreTicketReservationActivity
        Log.d("onActivityResult TAG","on activity result tag completed in ticketScreenAct");
       // this.stylist_list = intent.getParcelableArrayListExtra("stylist_list");

        this.tickets = intent.getParcelableArrayListExtra("tickets");
       // this.store = intent.getParcelableExtra("store");
        this.stylist_position =intent.getIntExtra("stylist_position",-1);

        super.onActivityResult(requestCode,resultCode,intent);

    }
}
    private void showCreditCard() {
        CreditCardDialog ccd=new CreditCardDialog(this, ws);
        ccd.showCreditCardDialog(true);
    }

    public void updateListView() {
        if(stylist_list.size()==0){
            stylist_list = store.getStylistList();
        }
        for(Ticket t : tickets){//linear runtime
            Stylist sty = store.getStylistHashMap().get(t.getStylist_id());//get the stylist of ticket
            if(sty != null) {
                sty.setWait(Integer.valueOf(t.getTicket()));//assume the last known record is the current max
                store.updateStylist(sty);//update stylist
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

            String link = "http://acbasoftware.com/pos/store_firebase.php";
            String data = URLEncoder.encode("store_firebase", "UTF-8") + "=" + URLEncoder.encode(Encryption.encryptPassword("acbastorelistacba"), "UTF-8");

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


        try {
           // Log.e("Web SERVER RESPONSE::",result);
            Map<String,Object> store_map = new HashMap<>();
              //  ArrayList<Store> store_firebase = new ArrayList<>();
            JSONObject jObject = new JSONObject(result);
            HashMap<String,Stylist> hm = new HashMap<>();

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

            JSONArray jArray = jObject.getJSONArray("store_firebase");
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
                    Store s = new Store(i,name, addr, citystate, phone, lat, lon,i,i,ticket_price,open,close,cprice,email,password,current_ticket,card_id,subscription_id);
                    //store_firebase.add(s);
                    s.setStylistHashMap(hm);
                    store_map.put(""+i,s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }///////end for
           // Map<String, Object> hashtaghMap = new ObjectMapper().convertValue(hashtaghModel, Map.class);
        //update just make unstatic   // myRef.updateChildren(store_map); //push cretes a new timestamp in the directory every time i call it
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
    private Store store_firebase;
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
        }
    }


    @Override
    protected Void doInBackground(final String... strings) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        //roots order by child get the Store JSON and gets the value email
        reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // dataSnapshot is the "issue" node with all children with id 0
               boolean found = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {//will produce all of the keys in firebase db
                    // Log.e("IN LOOP FIREBASE:: ",""+dds.getKey());
                    // do something with the individual "key"
                   Store s = ds.getValue(Store.class);
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
                       store_firebase.addStoreStylist();
                       // store_firebase.setStylistList(list);
                        //  store_firebase.setStylistList(l);
                        found = true;
                   }

                }
                if (found) {///set up program like like to database

                   act.myRef=act.database.getReference().child(store_firebase.getStore_number()+"").child("tickets");//refernce to all tickets for the store_firebase Store.
                     FirebaseWebTasks.downloadImages(act,store_firebase,act.stylist_list, pd);

                    store_firebase.setStylistList(act.stylist_list);//hopefullys stores the stylist
                    act.store= store_firebase;
                    // Read from the database
                    act.myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot == null || dataSnapshot.getValue()==null){return;}
                            Log.d("FIREBASE LISTENER", "Value UPDATEDDDDD...."+dataSnapshot.getValue().toString());
                            GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                            List<Ticket> current_tickets  = dataSnapshot.getValue(gti);
                            //update current store_firebase absolute ticket #
                            //update stores current ticket list but as of now im not storing that just displaying

                            act.tickets = new ArrayList<Ticket>(current_tickets);//finalize the updating
                            act.updateListView();

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w("FIREBASE ERROR", "Failed to read value.", error.toException());
                        }
                    });
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
    @Override
    protected void onPostExecute(Void result) {
        ///all handled in the thread doInBackground..
    }


    /*
    private class ListViewAdpaterTicket extends ArrayAdapter<Ticket> {

        // private boolean saveImage=false;
        public ListViewAdpaterTicket(Context c, int list_view_live_feed, ArrayList<Ticket> values) {
            super(c, list_view_live_feed, values);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            //View rowView = inflater.inflate(R.layout.list_view_live_feed, parent, false);
            Ticket t = getItem(position);

            return convertView;
        }
        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }
    }*/


}
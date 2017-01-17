package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.FirebaseWebTasks.FirebaseWebTasks;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;

import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.STYLIST_FIREBASE_URL;
import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.store_id;

/**
 * Main display for customer. Self kiosk serve
 */
public class TicketScreenActivity extends AppCompatActivity {
    public static final String STYLIST_FIREBASE_URL="images/stylists/";
    private WebService ws;
    private String TAG="FIREBASE TAG::";
    public static StorageReference mStorageRef;
    public static FirebaseDatabase database;
    private String email;
    private String password;
    public static ArrayList<Stylist> stylist_list;
    public static String store_id=null;
    public static int stylist_postion=0;
    public static DatabaseReference myRef;

    //public Store store;
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("store_id", TicketScreenActivity.store_id);
        outState.putParcelableArrayList("stylist_list", stylist_list);
        outState.putString("email", email);
        outState.putString("password", password);
        outState.putInt("stylist_position", stylist_postion);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_screen);

        if(savedInstanceState != null) {
            store_id=savedInstanceState.getString("store_id");
            stylist_list=savedInstanceState.getParcelableArrayList("stylist_list");
            email=savedInstanceState.getString("email");
            password=savedInstanceState.getString("password");
            stylist_postion=savedInstanceState.getInt("stylist_position");
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
        ClientWebTask cwt=new ClientWebTask(this, email, password, pd);
        cwt.execute();
        stylist_postion=0;
        // this.ws=new WebService();
        database=FirebaseDatabase.getInstance();
        mStorageRef=FirebaseStorage.getInstance().getReference();
        myRef=database.getReference();//.getReference("message");

// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Stylist value=dataSnapshot.getValue(Stylist.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private void writeUserData(int userId, String name) {
        //database.getReference().
    }

    @Override
    public void onBackPressed() {
        Log.d("Back Press in Ticket", "pressed");
    }

    private void reserve() {
        Intent i=new Intent(this, InStoreTicketReservationActivity.class);
        this.startActivity(i);
    }

    private void showCreditCard() {
        CreditCardDialog ccd=new CreditCardDialog(this, ws);
        ccd.showCreditCardDialog(true);
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
            return new String("Exception: " + e.getMessage());////no store id...
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

            /**   StorageReference sr=TicketScreenActivity.mStorageRef.child(store_id + "/" + STYLIST_FIREBASE_URL);

             for(final Stylist s : TicketScreenActivity.stylist_list) {
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
             }*/


            for(Stylist s : TicketScreenActivity.stylist_list) {
                FirebaseWebTasks.uploadImage(s.getImage(), s.getID());
            }

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

            byte[] qrimageBytes=Base64.decode(qrimage.getBytes(), Base64.DEFAULT);

            Bitmap bmp=BitmapFactory.decodeByteArray(qrimageBytes, 0, qrimageBytes.length);//Utils.resize(BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length),100,100);
            String store_id=jobj.getString("store_id");
            // Bitmap myBitmap = jobj.getby

            Stylist s=new Stylist(stylist_id, fname, mname, lname, available, bmp, null, store_id);
            if(TicketScreenActivity.store_id == null) {
                TicketScreenActivity.store_id=store_id;
            }
            TicketScreenActivity.stylist_list.add(s);//add stylist
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

class FirebaseStylist {
    private String name;
    private String pic;
    private String id;
    private boolean avail;
    private int wait;

    public FirebaseStylist(Stylist s) {
        this.name=s.getName();
        this.pic=s.getImage().toString();
        this.id=s.getID();
        this.avail=s.isAvailable();
        this.wait=s.getWait();
    }
}
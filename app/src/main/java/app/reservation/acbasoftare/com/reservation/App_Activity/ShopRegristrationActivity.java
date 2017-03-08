package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseEmployee;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStoreMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.App_Objects.ParamPair;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;

public class ShopRegristrationActivity extends FragmentActivity {


    private GoogleApiClient mGoogleApiClient;
    private Place place;
    private AutoCompleteTextView owner_name;
    private AutoCompleteTextView mobile_phone;
    private AutoCompleteTextView email;
    private boolean FIREBASE_ACCESS;

    @Override
    public void onBackPressed() {
        this.startActivity(new Intent(this, LoginActivity.class));
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FIREBASE_ACCESS = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_regristration);

        final TextView shop = (TextView) this.findViewById(R.id.selectedShop_textview);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.e("ERRR", "FAILED TO CONNECT GOOGLE API CLIENT");
                finish();
            }
        })
                .addApi(Places.GEO_DATA_API)
                .build();

        //
        place = null;

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {


                // TODO: Get info about the selected place.
              //  Log.i("PLACE: ", "Place: " + place.getName() + "\n" + place.getAttributions() + "\n" + place.getWebsiteUri());
                shop.setText(place.getName().toString().toUpperCase());
                ShopRegristrationActivity.this.place = place;
                /*Places.GeoDataApi.getPlaceById(mGoogleApiClient, place.getId())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                    final Place myPlace = places.get(0);
                                    Log.i("PLACE API:", "Place found: " + myPlace);
                                } else {
                                    Log.e("PLACE API: ", "Place not found");
                                }
                                places.release();
                            }
                        });*/
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("ERR: ", "An error occurred: " + status);
            }


        });///////end fragment
////////////////
        //spinner monday open = smo
        //spinner monday close = smc
        String[] values = new String[25];
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat ampm = new SimpleDateFormat("h:mm a");
        values[0] = "N/A";
        for (int i = 0; i < 24; ++i) {
            try {
                Date d = sdf.parse(i + ":00");
                String time = ampm.format(d);
                values[i + 1] = time;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //smo = spinner monday open
        // smc = spinner monday close

        MainActivity.SpinnerDropDownAdapter a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner smo = (Spinner) findViewById(R.id.spinner_mon_open);
        smo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner smc = (Spinner) findViewById(R.id.spinner_mon_close);
        smc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner sto = (Spinner) findViewById(R.id.spinner_tues_open);
        sto.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner stc = (Spinner) findViewById(R.id.spinner_tues_close);
        stc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner swo = (Spinner) findViewById(R.id.spinner_wed_open);
        swo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner swc = (Spinner) findViewById(R.id.spinner_wed_close);
        swc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner stho = (Spinner) findViewById(R.id.spinner_thurs_open);
        stho.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner sthc = (Spinner) findViewById(R.id.spinner_thurs_close);
        sthc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner sfo = (Spinner) findViewById(R.id.spinner_fri_open);
        sfo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner sfc = (Spinner) findViewById(R.id.spinner_fri_close);
        sfc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner sso = (Spinner) findViewById(R.id.spinner_sat_open);
        sso.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner ssc = (Spinner) findViewById(R.id.spinner_sat_close);
        ssc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner ssuno = (Spinner) findViewById(R.id.spinner_sun_open);
        ssuno.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this, R.id.spinnerDropDownTF, values);
        final Spinner ssunc = (Spinner) findViewById(R.id.spinner_sun_close);
        ssunc.setAdapter(a);

        email = (AutoCompleteTextView) this.findViewById(R.id.email_shopAct);
        final AutoCompleteTextView pass = (AutoCompleteTextView) this.findViewById(R.id.pass_shopAct);
        owner_name = (AutoCompleteTextView) this.findViewById(R.id.owner_name_acv);
        mobile_phone = (AutoCompleteTextView) this.findViewById(R.id.mobilephone_shopreg_activity);

       /* RelativeLayout lay = (RelativeLayout)this.findViewById(R.id.rel_layout) ;
        for(int i =0 ; i< lay.getChildCount(); i++){
            View v = lay.getChildAt(i);
            if(v instanceof Spinner){
                Spinner s = (Spinner) v;
                s.setTa
            }

        }*/
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FIREBASE_ACCESS = true;
                }
            }
        });

        Button reg = (Button) this.findViewById(R.id.btn_register_activity);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FIREBASE_ACCESS == false){
                    Toast.makeText(ShopRegristrationActivity.this,"Access denied. Contact ACBA for help.",Toast.LENGTH_LONG).show();
                }
                boolean err = true;
                if (ShopRegristrationActivity.this.place == null) {
                    Toast.makeText(ShopRegristrationActivity.this, "Oops. Select your shop from the Google Search bar on top.", Toast.LENGTH_LONG).show();
                } else if (!isValidName(owner_name.getText().toString())) {
                    owner_name.setError("Enter your full name");
                } else if (!isValidEmail(email.getText().toString())) {
                    email.setError("Enter a valid email");
                } else if (!isValidPassword(pass.getText().toString())) {
                    pass.setError("Password must be at least 6 characters or longer");
                } else if (!isValidPhone(mobile_phone.getText().toString())) {
                    mobile_phone.setError("Enter a valid phone number");
                } else {
                    err = false;
                }
                if (err) {
                    return;
                } else {
                    email.setError(null);
                    pass.setError(null);
                    owner_name.setError(null);
                    mobile_phone.setError(null);
                }

                final ProgressDialog pd = ProgressDialog.show(ShopRegristrationActivity.this, "Registering", "Please wait...", true, false);
                pd.show();

                final Map<String, String> period = new HashMap<String, String>();
                period.put(1 + "", smo.getSelectedItem() + "-" + smc.getSelectedItem());
                period.put(2 + "", sto.getSelectedItem() + "-" + stc.getSelectedItem());
                period.put(3 + "", swo.getSelectedItem() + "-" + swc.getSelectedItem());
                period.put(4 + "", stho.getSelectedItem() + "-" + sthc.getSelectedItem());
                period.put(5 + "", sfo.getSelectedItem() + "-" + sfc.getSelectedItem());
                period.put(6 + "", sso.getSelectedItem() + "-" + ssc.getSelectedItem());
                period.put(7 + "", ssuno.getSelectedItem() + "-" + ssunc.getSelectedItem());

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //NEED TO CHANGE THE LIST INTO A MAP later....
                        GenericTypeIndicator<List<FirebaseStore>> gti = new GenericTypeIndicator<List<FirebaseStore>>() {};
                        List<FirebaseStore> map = null;
                        if(dataSnapshot.getValue() == null){
                            map = new ArrayList<FirebaseStore>();
                            //no shops here...
                        }else {
                            Log.d("CLASS: ",dataSnapshot.getValue().toString()+"\n"+dataSnapshot.getValue().getClass().getClassLoader()+"<<class");
                            map = dataSnapshot.getValue(gti);
                        }

                        FirebaseStore store = new FirebaseStore(place, map.size(), email.getText().toString(), pass.getText().toString(), period);
                        for (int i = 0; i < map.size(); ++i) {
                            if (map.get(i) == null) {
                                map.remove(i);
                            }
                        }


                        int index = map.indexOf(store);
                        if (index >= 0) {
                            store = map.get(index);
                            Log.e("SHOP EXISTS: ", "#: " + store.getStore_number());
                            pd.dismiss();
                            Toast.makeText(ShopRegristrationActivity.this, "Store already exists. Please contact ACBA for more help.", Toast.LENGTH_LONG).show();
                        } else {
                            createStore(store, ref, pd);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                        Log.e("in on cancelled", "cancelled");
                    }
                });

            }
        });


    }

    private boolean isValidPhone(String s) {
        return s.length() == 10 && !s.contains(" ");
    }

    /**
     * CREATES A NEW STORE FOR FIREBASE: URL: user/store_number/{firebase_store_obj}
     *
     * @param store   firebase store to be added
     * @param ref the firebase url to add to
     * @param pd  register progress dialog
     */
    private void createStore( final FirebaseStore store, DatabaseReference ref, final ProgressDialog pd) {

        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                List<FirebaseStore> list = null;
                GenericTypeIndicator<List<FirebaseStore>> gti = new GenericTypeIndicator<List<FirebaseStore>>() {};

                if(mutableData.getValue() == null){//nothing there
                    list = new ArrayList<FirebaseStore>();
                }else{
                    list = mutableData.getValue(gti);
                }
                list.add(store);
                mutableData.setValue(list);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                String msg = "";
                if(databaseError != null && databaseError.toException()!=null){
                    msg = "Something went wrong :(. No data was saved";
                }else{
                    msg = "Store was successfully added!";
                    // sendEmail(store);
                }
                Toast.makeText(ShopRegristrationActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
        final FirebaseEmployee owner = new FirebaseEmployee(store, owner_name.getText().toString(), FirebaseEmployee.TYPE.OWNER);
        //create stylists for the app login. LIST<FIREBASEEMPLOYEE>. URL: employees/

        //add the owner and store no pref as stylists...in firebase url
        final Stylist no_pref = new Stylist(store, true);
        final Stylist owner_sty = new Stylist(store, owner_name.getText().toString(), email.getText().toString(), mobile_phone.getText().toString());

        DatabaseReference sty = FirebaseDatabase.getInstance().getReference().child("stylists/" + store.getStore_number());
        sty.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
               GenericTypeIndicator<Map<String,Stylist>> gti = new GenericTypeIndicator<Map<String, Stylist>>() {};
                Map<String,Stylist> map = null;
                if(mutableData.getValue() == null){
                    map = new HashMap<String, Stylist>();
                }else{
                    map = mutableData.getValue(gti);
                }
                map.put(no_pref.getId(),no_pref);
                map.put(owner_sty.getId(),owner_sty);
                mutableData.setValue(map);
                return Transaction.success(mutableData);
            }
            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                if(databaseError != null && databaseError.toException()!=null){
                    //err
                    Log.e("Err","creating stylists");
                    databaseError.toException().printStackTrace();
                }else{
                    Log.e("Success","stylsits created");
                }
            }
        });////end add stylists

        //add store to <user-meta-data>.. this allows for the app to get just the store'store : google_id, phone, store_number and geo locations coordinmates

        final FirebaseStoreMetaData meta = new FirebaseStoreMetaData(store,place);

        DatabaseReference user_meta_data = FirebaseDatabase.getInstance().getReference().child("user-meta-data/");
        user_meta_data.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                GenericTypeIndicator<List<FirebaseStoreMetaData>> gti = new GenericTypeIndicator<List<FirebaseStoreMetaData>>() {};
                List<FirebaseStoreMetaData> list = null;
                if(mutableData.getValue() == null){
                    list = new ArrayList<FirebaseStoreMetaData>();
                }else {
                    list = mutableData.getValue(gti);
                }

                if(!list.contains(meta)) {
                    list.add(meta);
                }
                mutableData.setValue(list);
                return  Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        if(databaseError!= null && databaseError.toException()!=null){
                            Log.e("ERROR USER_META_DATA","err user-meta-data....");
                            databaseError.toException().printStackTrace();
                        }else {
                            Log.e("success","meta data created");
                        }
            }
        });////end add meta-user

        final DatabaseReference emp_app = FirebaseDatabase.getInstance().getReference().child("employees/");
        emp_app.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                GenericTypeIndicator<Map<String,FirebaseEmployee>> gti = new GenericTypeIndicator<Map<String, FirebaseEmployee>>() {};
                Map<String,FirebaseEmployee> map = null;
                if(mutableData.getValue() == null){
                    map = new HashMap<String, FirebaseEmployee>();
                }else {
                    map = mutableData.getValue(gti);
                }
                map.put(String.valueOf(owner.getApp_username().toString().hashCode()),owner);
                mutableData.setValue(map);
                return  Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                pd.dismiss();
                if(databaseError != null && databaseError.toException() != null){
                    Log.e("Err","account not set up");
                    databaseError.toException().printStackTrace();
                    Log.e("err code: ",databaseError.getCode()+" ::: "+databaseError.getMessage()+" <mess> "+databaseError.getDetails());
                    Toast.makeText(ShopRegristrationActivity.this,"Oops. Something went wrong. Contact ACBA for help.",Toast.LENGTH_LONG).show(); //error
                }else{
                    Log.e("Success","Account created");
                    gotToEmployeeActivity(owner);
                }
            }
        }); /////////////added the owner

    }
    private void gotToEmployeeActivity(FirebaseEmployee owner, ProgressDialog pd) {
        if(pd.isShowing()){
            pd.dismiss();
        }
        Intent i = new Intent(ShopRegristrationActivity.this, EmployeeActivity.class);
        i.putExtra("employee", owner);
        ShopRegristrationActivity.this.startActivity(i);
        this.finish();
    }

    private void gotToEmployeeActivity(FirebaseEmployee owner) {
        Intent i = new Intent(ShopRegristrationActivity.this, EmployeeActivity.class);
        i.putExtra("employee", owner);
        ShopRegristrationActivity.this.startActivity(i);
        this.finish();
    }

    private boolean isValidName(String name) {
        return name != null && name.length() > 0;
    }

    private void sendEmail(final FirebaseStore store) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WebService ws = new WebService();
                ArrayList<ParamPair> param = new ArrayList<ParamPair>();
                param.add(new ParamPair("email", store.getEmail()));
                //param.ad
                ws.makeHTTPSConnection(WebService.emailURL, param);
            }
        }).start();
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isValidPassword(String pass) {
        return pass.length() > 5;
    }
}

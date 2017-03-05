package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
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
import com.google.api.client.util.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseEmployee;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
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

    @Override
    public void onBackPressed(){
        this.startActivity(new Intent(this,LoginActivity.class));
        this.finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_regristration);

        final TextView shop = (TextView)this.findViewById(R.id.selectedShop_textview);
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.e("ERRR","FAILED TO CONNECT GOOGLE API CLIENT");
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
                Log.i("PLACE: ", "Place: " + place.getName() + "\n" + place.getAttributions()+"\n"+place.getWebsiteUri());
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
        for (int i =0 ; i < 24; ++i){
            try {
                Date d = sdf.parse(i+":00");
                String time = ampm.format(d);
                values[i+1] = time;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //smo = spinner monday open
        // smc = spinner monday close

        MainActivity.SpinnerDropDownAdapter a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner smo = (Spinner)findViewById(R.id.spinner_mon_open);
        smo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner smc = (Spinner)findViewById(R.id.spinner_mon_close);
        smc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner sto = (Spinner)findViewById(R.id.spinner_tues_open);
        sto.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner stc = (Spinner)findViewById(R.id.spinner_tues_close);
        stc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner swo = (Spinner)findViewById(R.id.spinner_wed_open);
        swo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner swc = (Spinner)findViewById(R.id.spinner_wed_close);
        swc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner stho = (Spinner)findViewById(R.id.spinner_thurs_open);
        stho.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner sthc = (Spinner)findViewById(R.id.spinner_thurs_close);
        sthc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner sfo = (Spinner)findViewById(R.id.spinner_fri_open);
        sfo.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner sfc = (Spinner)findViewById(R.id.spinner_fri_close);
        sfc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner sso = (Spinner)findViewById(R.id.spinner_sat_open);
        sso.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner ssc = (Spinner)findViewById(R.id.spinner_sat_close);
        ssc.setAdapter(a);

        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner ssuno = (Spinner)findViewById(R.id.spinner_sun_open);
        ssuno.setAdapter(a);
        a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        final Spinner ssunc = (Spinner)findViewById(R.id.spinner_sun_close);
        ssunc.setAdapter(a);

          email = (AutoCompleteTextView)this.findViewById(R.id.email_shopAct);
        final AutoCompleteTextView pass = (AutoCompleteTextView)this.findViewById(R.id.pass_shopAct);
         owner_name = (AutoCompleteTextView)this.findViewById(R.id.owner_name_acv);
        mobile_phone = (AutoCompleteTextView)this.findViewById(R.id.mobilephone_shopreg_activity);

       /* RelativeLayout lay = (RelativeLayout)this.findViewById(R.id.rel_layout) ;
        for(int i =0 ; i< lay.getChildCount(); i++){
            View v = lay.getChildAt(i);
            if(v instanceof Spinner){
                Spinner s = (Spinner) v;
                s.setTa
            }

        }*/



        Button reg = (Button)this.findViewById(R.id.btn_register_activity);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean err = true;
                if(ShopRegristrationActivity.this.place == null){
                    Toast.makeText(ShopRegristrationActivity.this,"Oops. Select your shop from the Google Search bar on top.",Toast.LENGTH_LONG).show();
                }else if(!isValidName(owner_name.getText().toString())){
                    owner_name.setError("Enter your full name");
                }else if(!isValidEmail(email.getText().toString())){
                    email.setError("Enter a valid email");
                }else if(!isValidPassword(pass.getText().toString())){
                    pass.setError("Password must be at least 6 characters or longer");
                }else
                {
                    err = false;
                }
                if (err){
                    return;
                }else {
                    email.setError(null);
                    pass.setError(null);
                    owner_name.setError(null);
                }

                final ProgressDialog pd = ProgressDialog.show(ShopRegristrationActivity.this,"Registering","Please wait...",true,false);
                pd.show();

                final Map<String,String> period = new HashMap<String, String>();
                period.put(1+"",smo.getSelectedItem()+"-"+smc.getSelectedItem());
                period.put(2+"",sto.getSelectedItem()+"-"+stc.getSelectedItem());
                period.put(3+"",swo.getSelectedItem()+"-"+swc.getSelectedItem());
                period.put(4+"",stho.getSelectedItem()+"-"+sthc.getSelectedItem());
                period.put(5+"",sfo.getSelectedItem()+"-"+sfc.getSelectedItem());
                period.put(6+"",sso.getSelectedItem()+"-"+ssc.getSelectedItem());
                period.put(7+"",ssuno.getSelectedItem()+"-"+ssunc.getSelectedItem());

                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");
                ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<List<FirebaseStore>> gti = new GenericTypeIndicator<List<FirebaseStore>>() {};

                        List<FirebaseStore> map = dataSnapshot.getValue(gti);

                        FirebaseStore s = new FirebaseStore(place,map.size(),email.getText().toString(),pass.getText().toString(),period);
                        for(int i = 0; i< map.size(); ++i){
                            if(map.get(i)==null){
                                map.remove(i);
                            }
                        }


                        int index = map.indexOf(s);
                        if(map.contains(s)){
                            s = map.get(index);
                            Log.e("SHOP EXISTS: ","#: "+s.getStore_number());
                            pd.dismiss();
                            Toast.makeText(ShopRegristrationActivity.this,"Store already exists. Please contact ACBA for more help.",Toast.LENGTH_LONG).show();
                        }else {
                            createStore(map, s, ref, pd);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.e("in on cancelled","cancelled");
                    }
                });

            }
        });


    }

    /**
     * CREATES A NEW STORE FOR FIREBASE: URL: user/store_number/{firebase_store_obj}
     * @param map arraylist of valid stores.
     * @param s firebase store to be added
     * @param ref the firebase url to add to
     * @param pd register progress dialog
     */
    private void createStore(List<FirebaseStore> map, final FirebaseStore s, DatabaseReference ref, final ProgressDialog pd) {
        map.add(s);

        ref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                String msg = "";
                if(task.isSuccessful()){
                    msg = "Store was successfully added!";
                    //sendEmail(s);
                }else{
                    msg = "Something went wrong :(. No data was saved";
                }
                Toast.makeText(ShopRegristrationActivity.this,msg,Toast.LENGTH_LONG).show();
            }
        });
        //create stylists for the app login. LIST<FIREBASEEMPLOYEE>. URL: employees/
        final DatabaseReference emp_app = FirebaseDatabase.getInstance().getReference().child("employees");
        emp_app.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<FirebaseEmployee>> gti = new GenericTypeIndicator<List<FirebaseEmployee>>() {};
                List<FirebaseEmployee> list = dataSnapshot.getValue(gti);
                //add store_emp
                //add owner
                FirebaseEmployee owner = new FirebaseEmployee(s,owner_name.getText().toString(), FirebaseEmployee.TYPE.OWNER);
                list.add(owner);
                emp_app.setValue(list).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.e("account made","owner added!");
                        }else{
                            Log.e("account error","check logs");
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });/////////////added the owner
        //add the owner and store no pref as stylists...in firebase url

        final DatabaseReference sty = FirebaseDatabase.getInstance().getReference().child("stylists/"+s.getStore_number());
        sty.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){//empty
                    Stylist no_pref = new Stylist(s,true);
                    Stylist owner = new Stylist(s,owner_name.getText().toString(),email.getText().toString(),mobile_phone.getText().toString());
                    Map<String, Stylist> map = new HashMap<String, Stylist>();
                    map.put(no_pref.getId(),no_pref);
                    map.put(owner.getId(),owner);
                    sty.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.e("Stylists reg","success added 2 stylists");
                            }else{
                                Log.e("Error adding stylits"," error registering stylists data null");
                            }
                        }
                    });
                    return;
                }else {
                    GenericTypeIndicator<Map<String, Stylist>> gti = new GenericTypeIndicator<Map<String, Stylist>>() {};
                    Map<String, Stylist> map = dataSnapshot.getValue(gti);
                    Stylist no_pref = new Stylist(s,true);
                    Stylist owner = new Stylist(s,owner_name.getText().toString(),email.getText().toString(),mobile_phone.getText().toString());
                    map.put(no_pref.getId(),no_pref);
                    map.put(owner.getId(),owner);
                    sty.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.e("Stylists reg","success added map");
                            }else{
                                Log.e("Error adding stylits"," error registering stylists");
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
private boolean isValidName(String name){
    return name!=null && name.length()>0;
}
private void sendEmail(FirebaseStore store){
    new Thread(new Runnable() {
        @Override
        public void run() {
            WebService ws = new WebService();
            ArrayList<ParamPair> param = new ArrayList<ParamPair>();
           // param.add(new ParamPair())
            ws.makeHTTPSConnection(WebService.emailURL, param);
        }
    }).start();
}
    private boolean isValidEmail(String email){
        return email.contains("@") && email.contains(".");
    }
    private boolean isValidPassword(String pass){
        return pass.length() > 5 ;
    }
}

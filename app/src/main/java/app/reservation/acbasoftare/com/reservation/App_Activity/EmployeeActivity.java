package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseEmployee;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 *EmployeeActivity is responsibe for all Stylist account functionality. For example, Stylist customize their profile, read messages from clients, notifications  etc.
 * 3 type of accounts- Owner of saloons, stylists, clients. The 2 former are all controlled here. The client is controlled by MainActivity,java class.
 *
 */

public class EmployeeActivity extends AppCompatActivity {
   // private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    // public static Reservation reservation;
    // public static String stylist_id;
    // public static String store_id;
    //  public static EmployeeActivity employeeActivity;
    private SectionsPagerAdapter mCustomFragPageAdapter;
    private TabLayout tabLayout;
    //  public static Stylist stylist;
    //  public static View tab3, tab2;
    //public static ExpandableListViewAdapter lva;
    private FirebaseEmployee firebaseEmployee;
    public static Bitmap empBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        this.firebaseEmployee = this.getIntent().getParcelableExtra("employee");
        //Log.e("Emppp:",firebaseEmployee.toString());
        // employeeActivity=this;
        // store_id=getIntent().getStringExtra("store_id");
        // stylist_id=getIntent().getStringExtra("stylist_id");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
      //  mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
      //  mViewPager = (ViewPager) findViewById(R.id.container);
       // mViewPager.setAdapter(mSectionsPagerAdapter);
///////////////////////////////////////////////////////////////
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_employeeactivity);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return store_list fragment for each of the three
        // primary sections of the activity.
        mCustomFragPageAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCustomFragPageAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs_employeeactivity);
        tabLayout.setupWithViewPager(mViewPager);

        /////////////////////////

    }

    /*private void fragmentTab2() {
        ListView lv=(ListView) tab2.findViewById(R.id.listview_employee_activity);
        ArrayList<String> days=reservation.getDaysReserved();//for given stylist
        DateArrayAdapter la=new DateArrayAdapter(this, R.layout.expandable_list_item, days);
        lv.setAdapter(la);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_employee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.log_out) {
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            String PREF_USERNAME =  this.getIntent().getStringExtra("PREF_USERNAME");
            String PREF_PASSWORD = this.getIntent().getStringExtra("PREF_PASSWORD");
            pref.edit().putString(PREF_USERNAME, null).putString(PREF_PASSWORD, null).commit();//debug clear memory of use
            Intent i = new Intent(this, LoginActivity.class);
            startActivity (i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //public static String getStoreID() {return store_id;}

    //public static String getStylistID() {return stylist_id;}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements EmployeeData {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private EmployeeActivity ea;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //that holds all components(Views) for the tab screens
            int page = this.getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (page - 1) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_employee_settings_current_tickets, container, false); //inflater.inflate(R.layout.fragment_employee_today_layout, container, false);///this is the fragment view
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_employee_activity_settings_layout, container, false);  //rootView = inflater.inflate(R.layout.fragment_employee_upcoming_layout, container, false);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_employee_activity_store_settings_layout, container, false);
                    break;
            }
            return displayView(rootView, page - 1);
        }

        /*This method is responsible for displaying the appropriate Views
              * I guess the layouts are formed before even switching to a tab, so on startup up...
              * */
        private View displayView(View rootView, int page) {

            switch (page) {
                case 0:
                    fragmentStoreList(rootView);//fragmentView0(rootView);
                    break;
                case 1:
                    fragmentView2(rootView);
                    break;
                case 2:
                    fragmentViewStoreSettings(rootView);
                    break;
            }
            return rootView;
        }

        private void fragmentViewStoreSettings(View rootView) {
            final String storeURL = "user/"+ea.firebaseEmployee.getStore_number();
            final DecimalFormat df = new DecimalFormat("$0.00");
            final TextView price = (TextView)rootView.findViewById(R.id.ticketprice_Textview);


            Button update = (Button)rootView.findViewById(R.id.updateTicketBTN);
            final EditText ticket_et = (EditText)rootView.findViewById(R.id.editTextTicketPrice);
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ticket_et.getText().length()==0){
                        ticket_et.setError("Specify amount");
                        return;
                    }
                    ticket_et.setError(null);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(storeURL+"/ticket_price");
                    ref.setValue(Double.valueOf(ticket_et.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                         String msg="";
                            if(task.isSuccessful()) {
                             msg = "Ticket price updated!";
                         }else{
                             msg = "Oops. Something went wrong.";
                         }
                            Toast.makeText(ea,msg,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });/////end update ticekt price
            DatabaseReference tprice_ref = FirebaseDatabase.getInstance().getReference().child(storeURL+"/ticket_price");
            tprice_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null)return;
                    Double val = dataSnapshot.getValue(Double.class);
                    price.setText(df.format(val));
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("err","ticket price err.");
                }
            });///end ticket price listner...
            ///add storage for this store
            Button addEmpBTN = (Button)rootView.findViewById(R.id.addStylistBTN);
            addEmpBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addEmployee();
                }
            });
            Button store_imageBTN = (Button)rootView.findViewById(R.id.uploadStorePicBTN);
            store_imageBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageGallery(2);
                }
            });
            final ImageView store_pic = (ImageView)rootView.findViewById(R.id.imageView_store_pic);
            final String  storageURL= ea.firebaseEmployee.getPhone()+"/images/stylists/-1";
            final StorageReference sr = FirebaseStorage.getInstance().getReference().child(storageURL);
            sr.getBytes(10*1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    store_pic.setImageBitmap(Utils.convertBytesToBitmap(bytes));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                   BitmapDrawable bd = (BitmapDrawable) ea.getResources().getDrawable(R.drawable.acba);
                    sr.putBytes(Utils.convertBitmapToByteArray(bd.getBitmap())).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                Log.e("Success","Stylist -1 added");
                            }else{
                                Log.e("ERR","Firebase uploading stylist -1 error");
                            }
                        }
                    });
                }
            });
        }////////////////////////////////////end method third tab

        /**
         * THIS method will create a Alert dialog with a layout that take the stylist NAME and EMAIL and will
         * GENERATE a user account and stylist for the shop.
         */
        private void addEmployee(){
            View rootView = ea.getLayoutInflater().inflate(R.layout.add_employee_layout,null,false);
            final String store_id = ea.firebaseEmployee.getStore_number();
            //show a alert dialog to let email of the stylist and defualt password is store phone_number
            final AutoCompleteTextView sty_name = (AutoCompleteTextView)rootView.findViewById(R.id.stylistname_actv_alert);

            final AutoCompleteTextView sty_email = (AutoCompleteTextView)rootView.findViewById(R.id.styEmailNewUser_atv_alert);



            new AlertDialog.Builder(ea).setTitle("New Stylist").setMessage("Create new user by entering the email of the stylist. The defualt password is your" +
                    " shop's phone number: "+ea.firebaseEmployee.getStore_phone()+". Once submitted your stylist is ready to login and customize their profile!")
                    .setView(rootView).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).setPositiveButton("Create Stylist", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String name = sty_name.getText().toString();
                    String id = Utils.generateID(sty_email.getText().toString());
                    String email = sty_email.getText().toString();
                    if(!Utils.isValidEmail(email)){
                        sty_email.setError("Enter a valid email");
                        return;
                    }
                    sty_email.setError(null);
                    final Stylist emp = new Stylist(name,id,store_id);
                    final FirebaseEmployee fe = new FirebaseEmployee(ea.firebaseEmployee,emp,email);
                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employees/");
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            GenericTypeIndicator<Map<String,FirebaseEmployee>> gti = new GenericTypeIndicator<Map<String, FirebaseEmployee>>() {};
                            Map<String, FirebaseEmployee>map = null;
                            if(mutableData.getValue() == null){//nothing there
                                map = new HashMap<String, FirebaseEmployee>();
                            }else{
                                map = mutableData.getValue(gti);
                            }
                            map.put(String.valueOf(fe.getApp_username().hashCode()),fe);
                            mutableData.setValue(map);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            Log.e("finished"," adding user to app.");
                        }
                    });/////////end add app to app
                    final String styURL = "stylists/"+fe.getStore_number()+"/";//stylists/store_number/{map of sty}
                    final DatabaseReference sty_ref = FirebaseDatabase.getInstance().getReference().child(styURL);
                    sty_ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            GenericTypeIndicator<Map<String,Stylist>> gti = new GenericTypeIndicator<Map<String, Stylist>>() {};
                            Map<String,Stylist> map = null;
                            if(mutableData.getValue() == null){
                                map = new HashMap<String, Stylist>();
                            }else{
                                map = mutableData.getValue(gti);
                            }
                            map.put(emp.getId(),emp);
                            mutableData.setValue(map);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                String msg = "";
                            if(databaseError!=null && databaseError.toException()!=null){
                                msg = "Oops. Something went wrong. Contact ACBA for help.";
                            }else{
                                msg = "Stylist added!";
                            }
                            Toast.makeText(ea,msg,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).show();
        }

        /**
         * FIRST TAB DISPLAYS ALL STORE TICKETS
         * @param rootView
         */
        private void fragmentStoreList(View rootView) {

           final ListView lv = (ListView) rootView.findViewById(R.id.mobile_tickets_lv);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets/"+ea.firebaseEmployee.getStore_number());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    lv.setAdapter(null);
                    if(dataSnapshot.getValue() == null){
                        return;
                    }
                    GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                    List<Ticket> list = dataSnapshot.getValue(gti);
                    EmployeeStoreTicketAdapter a = new EmployeeStoreTicketAdapter(ea,0,list);
                    lv.setAdapter(a);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                           Ticket t = (Ticket) adapterView.getItemAtPosition(pos);
                           // Toast.makeText(ea,"Are you sure want to delete this ticket_number #"+t.unique_id+"#? Action cannot be undone.",Toast.LENGTH_LONG).show();
                                showAlert(t);
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });//listener

        }

        private void showAlert(final Ticket t) {
            new AlertDialog.Builder(ea).setTitle("Delete Ticket #"+t.unique_id+"?").setMessage("This action cannot be undone").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets/"+ea.firebaseEmployee.getStore_number());
                    ref.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {

                            if(mutableData.getValue() == null){
                                return Transaction.success(mutableData);
                            }
                            GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                            List<Ticket> list = mutableData.getValue(gti);
                           // Log.e("list size!:: ","sizeee "+list.size()+"list:: "+list+" con::"+list.contains(t));
                            if(list.contains(t)){
                             //   Log.e("in contains!:: ","true comtainsd..");
                                list.remove(t);
                            }
                            //Log.e("after size!:: ",""+list.size());
                            mutableData.setValue(list);
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            determineCurrentTicket(t);
                            Log.e("on complete delete ...","on complete");
                        }
                    });
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //nothing
                }
            }).show();
        }

        private void determineCurrentTicket(final Ticket t) {
            DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("user/"+ea.firebaseEmployee.getStore_number()+"/current_ticket");
            r.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    if(mutableData.getValue() == null){
                        mutableData.setValue(t.unique_id);
                    }
                    long ct = mutableData.getValue(Long.class);
                    if(t.unique_id > ct){
                        mutableData.setValue(t.unique_id);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        Log.e("CT updated...","current t updated or at least callback..."); ///
                }
            });
        }

        private void fragmentView2(final View rootView) {

            if (ea.firebaseEmployee != null) {
                StorageReference sr = FirebaseStorage.getInstance().getReference().child(ea.firebaseEmployee.getStore_phone() + "/images/stylists/" + ea.firebaseEmployee.getId());
                sr.getBytes(1024 * 1024 * 10).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                         EmployeeActivity.empBitmap = Utils.convertBytesToBitmap(bytes);
                        ImageView iv = (ImageView) rootView.findViewById(R.id.imageView_employee_activity);
                        iv.setImageBitmap(EmployeeActivity.empBitmap);
                    }
                });
            }

            TextView tv = (TextView) rootView.findViewById(R.id.textView_tab3_employee_activity);
            tv.setText(tv.getText() + ea.firebaseEmployee.getName().toUpperCase());
            Button b = (Button) rootView.findViewById(R.id.button_upload_profile_pic);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageGallery(1);
                }
            });
            final Switch avail = (Switch) rootView.findViewById(R.id.switchEmp);
            this.ea.determineAvailable(avail);
            avail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final String path = "stylists/"+ea.firebaseEmployee.getStore_number()+"/"+ ea.firebaseEmployee.getId()+"/available";
                    final ProgressDialog pd = ProgressDialog.show(ea,"Updating Status","Please wait...",true,false);
                    pd.show();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
                    ref.setValue(avail.isChecked()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pd.dismiss();
                            Toast.makeText(ea,"Status updated!",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            avail.setChecked(!avail.isChecked());
                            pd.dismiss();
                            Toast.makeText(ea,"Uh oh something went wrong...Nothing changed.",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    });

                }
            });
            final TextView phoneTV = (TextView)rootView.findViewById(R.id.sty_phon_textview_empAct);
            phoneTV.setText(Utils.formatPhoneNumber(ea.firebaseEmployee.getPhone()));
            EditText phone = (EditText)rootView.findViewById(R.id.editTextEmployeePhone);
            TextWatcher tw = new TextWatcher() {
                public void afterTextChanged(Editable s){

                }
                public void  beforeTextChanged(CharSequence s, int start, int count, int after){
                    // you can check for enter key here
                }
                public void  onTextChanged (CharSequence s, int start, int before,int count) {
                    if(s.length()==10){
                        updatePhone(s,phoneTV);
                    }
                }
            };
            phone.addTextChangedListener(tw);


            Button reset = (Button) rootView.findViewById(R.id.resetTicketBtn);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(ea).setTitle("Reset Ticket Counter?").setMessage("This option will set the next available ticket to 1.").setIcon(ea.getResources().getDrawable(android.R.drawable.ic_dialog_alert)).
                            setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user/"+ea.firebaseEmployee.getStore_number()+"/current_ticket");
                                    ref.setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            String msg = "";
                                            if(task.isSuccessful()){
                                                msg = "Success! Current Ticket is now set to 1.";
                                            }else{
                                                msg = "Something went wrong :(. No data has changed.";
                                            }
                                            Toast.makeText(ea,msg,Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                }
            });//////////BUTON RESET TICKETS


            final TextView tprice = (TextView)rootView.findViewById(R.id.ticketPrice_employee_empACT);
            FirebaseDatabase.getInstance().getReference().child("stylists/"+ea.firebaseEmployee.getStore_phone()+"/"+ea.firebaseEmployee.getId()+"/ticket_price")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() == null){
                                tprice.setText("N/A");
                                return;
                            }
                            Double price = dataSnapshot.getValue(Double.class);
                            DecimalFormat df = new DecimalFormat("$ 0.00");
                            tprice.setText(df.format(price));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });/////////get the ticket price
            final AutoCompleteTextView password_tv = (AutoCompleteTextView)rootView.findViewById(R.id.password_actv_emp_act);
            password_tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                    if(action == EditorInfo.IME_ACTION_DONE){////DONE was pressed
                        if(!Utils.isValidPassword(password_tv.getText().toString())){
                            password_tv.setError("Passoword needs to be at least 6 characters.");
                            return false;
                        }
                        password_tv.setError(null);

                        View changePasswordView = ea.getLayoutInflater().inflate(R.layout.password_change_alert_view,null,false);
                        final EditText curr_pass_tf = (EditText)changePasswordView.findViewById(R.id.editText_current_password_alertview);
                        //create alert dialog
                        new AlertDialog.Builder(ea).setTitle("Password Change Requested").setMessage("Enter your current password to update new password.")
                                .setIcon(ea.getResources().getDrawable(android.R.drawable.ic_dialog_alert)).setView(changePasswordView).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setPositiveButton("Update Password", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String new_pass = Encryption.encryptPassword(password_tv.getText().toString());
                                String curr_pass = Encryption.encryptPassword(curr_pass_tf.getText().toString());
                                if(curr_pass.equals(ea.firebaseEmployee.getApp_password())){
                                    final String styURL = "employees/";
                                    final FirebaseEmployee fe = ea.firebaseEmployee;
                                    fe.setApp_password(new_pass);/////change password
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(styURL);
                                    ref.runTransaction(new Transaction.Handler() {
                                        @Override
                                        public Transaction.Result doTransaction(MutableData mutableData) {
                                            GenericTypeIndicator<Map<String,FirebaseEmployee>> gti = new GenericTypeIndicator<Map<String, FirebaseEmployee>>() {};
                                            Map<String,FirebaseEmployee> map = null;
                                            if(mutableData.getValue() == null){
                                                map = new HashMap<String, FirebaseEmployee>();
                                            }else{
                                                map = mutableData.getValue(gti);
                                            }
                                            map.put(Utils.generateID(fe.getApp_username()),fe);//update object
                                            mutableData.setValue(map);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                            String msg = "";
                                            if(databaseError!=null && databaseError.toException()!=null){
                                                databaseError.toException().printStackTrace();
                                                msg="Oops. Something went wrong. Contact ACBA password was not changed.";
                                            }else{
                                                msg = "Success! Password updated.";
                                            }
                                            Toast.makeText(ea,msg,Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }else{
                                    Toast.makeText(ea,"Password does not match current password. Call ACBA for help if you need.",Toast.LENGTH_LONG).show();
                                }
                            }
                        }).show();
                    }

                    return false;
                }
            });

        }

        /**
         * UPDATES PHONE NUMBER FROM FIREBASE URL: stylists/store_number/sty_id/phone/
         * @param phone
         */
        private void updatePhone(final CharSequence phone, final TextView phone_tv) {
            new AlertDialog.Builder(this.ea).setTitle("Update Phone?").setMessage("This option will update the phone number for the app to: "+phone+".")
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String path = "stylists/"+ea.firebaseEmployee.getStore_number()+"/"+ea.firebaseEmployee.getId()+"/phone";
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
                            ref.setValue(phone.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    phone_tv.setText(Utils.formatPhoneNumber(phone.toString()));
                                    Toast.makeText(ea,"Phone updated!",Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ea,"Error. Failed to update.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

        @Override
        public void setEmployeeActivity(EmployeeActivity ea) {
            this.ea = ea;
        }

        @Override
        public EmployeeActivity getEmployeeActivity() {
            return this.ea;
        }

        /*private void fragmentView0(View rootView) {
            TextView tv=(TextView) rootView.findViewById(R.id.textView_stylist_today_upcoming_header);
            SimpleDateFormat sdf=new SimpleDateFormat("MMM/dd/yyyy");
            tv.setText(tv.getText().toString() + sdf.format(new Date()));
            ExpandableListView lv=(ExpandableListView) rootView.findViewById(R.id.expandable_List_view);
            if(reservation != null && lva != null) {
                ProgressBar pb=(ProgressBar) rootView.findViewById(R.id.progressBar_emp_act);
                pb.setVisibility(View.GONE);
                lv.setAdapter(lva);
            } else {
                StylistWebTaskAppointments swt=new StylistWebTaskAppointments(rootView, lv);
                swt.execute();
            }
        }*/

        /**
         * requestCode ==1 then upload stylist image
         * requestCode == 2 then upload STORE image
         * @param requestCode
         * @param permissions
         * @param grantResults
         */
        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
           // Log.e("On Request Permission", "RequestCode: " + requestCode);

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent(requestCode);
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }

        public void openImageGallery(int result) {
            galleryIntent(result);
            if (Build.VERSION.SDK_INT >= 23) {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(this.ea.getApplicationContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ea,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        showReason();
                    } else {
                        ActivityCompat.requestPermissions(ea,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                result);
                    }
                } else {
                    galleryIntent(result);
                }
            } else {
                galleryIntent(result);
            }
        }

        /**
         * result == 1 then this will update the STYLIST's image
         * result == 2 then this will update the STORES image
         * @param result
         */
        private void galleryIntent(int result) {
            Intent intent = new Intent();
// Show only images, no videos or anything else
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
            ea.startActivityForResult(Intent.createChooser(intent, "Select Picture"), result);

        }

        private void showReason() {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ea);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Needed to upload profile picture.");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(ea, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        }
    }//end placeholder

    private void determineAvailable(final Switch switchAvail) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("stylists/"+firebaseEmployee.getStore_number()+"/"+firebaseEmployee.getId());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    switchAvail.setChecked(false);
                    return;
                }
                Stylist s = dataSnapshot.getValue(Stylist.class);
                switchAvail.setChecked(s.isAvailable());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * A {@link SectionsPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private EmployeeActivity ea;

      private   SparseArray<Fragment> registeredFragments ;
        public SectionsPagerAdapter(FragmentManager fragmentManager, EmployeeActivity emp) {
            super(fragmentManager);
            this.ea = emp;
            this.registeredFragments = new SparseArray<Fragment>();

        }
//////////////
        public Fragment getRegisteredFragment(int position) {
           // Log.e("is registerrrrr null?", String.valueOf(registeredFragments==null)+" size: "+registeredFragments.size());
            return registeredFragments.get(position);
        }

        //////
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            PlaceholderFragment pf = PlaceholderFragment.newInstance(position + 1);
            pf.setEmployeeActivity(ea);
            return pf;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
        @Override
        public int getCount() {
            if(ea.firebaseEmployee.getType().toUpperCase().equalsIgnoreCase(FirebaseEmployee.TYPE.OWNER.toString()))
            {
                return 3;
            }
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current Tickets";//"Today's Appointments";
                case 1:
                    return "User Settings";//"Upcoming Appointments";
                case 2:
                    return "Store Settings";
                /*case 2:
                    return "User Settings";*/
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public interface EmployeeData {
        void setEmployeeActivity(EmployeeActivity ea);

        EmployeeActivity getEmployeeActivity();

    }

    /**
     * Uploads new pic from device to firebase storeage...
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                  Fragment pf = this.mCustomFragPageAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
               // Log.e("pageeee: ## ", String.valueOf(pf==null));
                 ImageView imageView = (ImageView) pf.getView().findViewById(R.id.imageView_employee_activity);
                  imageView.setImageBitmap(bitmap);
                StorageReference sr = FirebaseStorage.getInstance().getReference().child(this.firebaseEmployee.getStore_phone()+"/images/stylists/"+this.firebaseEmployee.getId());
                // Get the data from an ImageView as bytes
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                 bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();

                UploadTask uploadTask = sr.putBytes(bytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EmployeeActivity.this,"Something went wrong... :(",Toast.LENGTH_LONG).show();    // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                       // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(EmployeeActivity.this,"Picture uploaded!",Toast.LENGTH_SHORT).show();
                    }
                });



                //   UploadImageWebTask u = new UploadImageWebTask(bitmap);
                //  u.execute();
                //stylist.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null){

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                Fragment pf = this.mCustomFragPageAdapter.getRegisteredFragment(mViewPager.getCurrentItem());
                // Log.e("pageeee: ## ", String.valueOf(pf==null));
                ImageView imageView = (ImageView) pf.getView().findViewById(R.id.imageView_store_pic);
                imageView.setImageBitmap(bitmap);
                StorageReference sr = FirebaseStorage.getInstance().getReference().child(this.firebaseEmployee.getStore_phone()+"/images/stylists/-1");
                // Get the data from an ImageView as bytes
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] bytes = baos.toByteArray();

                UploadTask uploadTask = sr.putBytes(bytes);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(EmployeeActivity.this,"Something went wrong... :(",Toast.LENGTH_LONG).show();    // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(EmployeeActivity.this,"Picture uploaded!",Toast.LENGTH_SHORT).show();
                    }
                });



                //   UploadImageWebTask u = new UploadImageWebTask(bitmap);
                //  u.execute();
                //stylist.setBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * THIS CLASS DISPLAYS THE TICKET LIST FOR LIST VIEW OF ALL TICKETS OF SHOP
     */
    static class EmployeeStoreTicketAdapter extends ArrayAdapter<Ticket> {
        private List<Ticket> list;

        public EmployeeStoreTicketAdapter(Context context, int resource, List<Ticket> t) {
            super(context, resource, t);
            this.list = t;
        }
        @Override
        public  int getCount(){
            return this.list.size();
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Ticket t = getItem(position);
            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.store_ticket_lv_layout, null);
            TextView tv = (TextView) view.findViewById(R.id.ticketNumber_tv);
            tv.setText(t.unique_id+"");
            TextView pref = (TextView) view.findViewById(R.id.stylist_pref_tv);
            pref.setText(t.stylist.toUpperCase());
            TextView name = (TextView) view.findViewById(R.id.client_name_tv);
            if(t.getName().length()==0){
                name.setText("N/A");
            }else {
                name.setText(t.getName().toUpperCase());
            }
            return view;
        }
    }
}

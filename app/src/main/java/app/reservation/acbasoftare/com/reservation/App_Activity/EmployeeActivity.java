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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.Reservation;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.ExpandableListView.ExpandableListViewAdapter;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.StylistWebTaskAppointments;
import app.reservation.acbasoftare.com.reservation.WebTasks.UploadImageWebTask;

import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.PREF_PASSWORD;
import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.PREF_USERNAME;

public class EmployeeActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public static Reservation reservation;
    public  String stylist_id;
    public  String store_id;
   // public static employee_activity employeeActivity;
    private SectionsPagerAdapter mCustomFragPageAdapter;
    private TabLayout tabLayout;
   // public static Stylist stylist;
   // public static View tab2;// tab3;
    public static ExpandableListViewAdapter lva;
    private Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
       // employeeActivity=this;
        store_id=getIntent().getStringExtra("store_id");
        stylist_id=getIntent().getStringExtra("stylist_id");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager(),EmployeeActivity.this);

        // Set up the ViewPager with the sections adapter.
        mViewPager=(ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
///////////////////////////////////////////////////////////////
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_employeeactivity);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return store_list fragment for each of the three
        // primary sections of the activity.
        /*Bundle b = new Bundle();
        b.putString("store_id",store_id);
        b.putString("stylist_id",stylist_id);*/
        mCustomFragPageAdapter=new EmployeeActivity.SectionsPagerAdapter(getSupportFragmentManager(),this);

        // Set up the ViewPager with the sections adapter.
        mViewPager=(ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCustomFragPageAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*Log.e("PAGE SELECTED::: ", String.valueOf(position));
                SectionsPagerAdapter sp  =  (SectionsPagerAdapter)mViewPager.getAdapter();
                Log.e("SP: Key[int]: ",""+sp.map.containsKey(position)+" <> is emp_act null? "+String.valueOf(sp.map.get(position).employee_activity==null));
               PlaceholderFragment p = sp.map.get(position);
                p.setEmployeeActivityData(EmployeeActivity.this);
                Log.e("PlaceHolder Frag: #",p.toString());*/

                if(position==1){

                }
                /*if(position == 1) {
                    if(reservation == null || lva == null || tab2 == null) return;//break if null
                    fragmentTab2();
                }*/
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout=(TabLayout) findViewById(R.id.tabs_employeeactivity);
        tabLayout.setupWithViewPager(mViewPager);

        /////////////////////////

    }
public Store getStore(){
    return this.store;
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
        int id=item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.log_out) {
            SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            pref.edit().putString(PREF_USERNAME, null).putString(PREF_PASSWORD, null).commit();//debug clear memory of use
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* public static String getStoreID() {return store_id;}

    public static String getStylistID() {return stylist_id;}*/

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements EmployeeActivityData {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER="section_number";
        private EmployeeActivity employee_activity;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment=new PlaceholderFragment();
            Bundle args=new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            //that holds all components(Views) for the tab screens
            int page=this.getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView=null;
            switch(page - 1) {
                case 0:
                    rootView =inflater.inflate(R.layout.fragment_employee_settings_current_tickets, container, false);///this is the fragment view
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_employee_activity_settings_layout, container, false);///this is the fragment view
                    ///tab2=rootView;
                    break;
               /* case 0:
                    rootView=inflater.inflate(R.layout.fragment_employee_today_layout, container, false);///this is the fragment view
                    break;
                case 1:
                    rootView=inflater.inflate(R.layout.fragment_employee_upcoming_layout, container, false);
                    tab2=rootView;
                    break;
                case 2:
                    rootView=inflater.inflate(R.layout.fragment_employee_activity_settings_layout, container, false);
                    tab3=rootView;
                    break;*/
            }
            return displayView(rootView, page - 1);
        }

        /*This method is responsible for displaying the appropriate Views
              * I guess the layouts are formed before even switching to a tab, so on startup up...
              * */
        private View displayView(View rootView, int page) {

            switch(page) {
                case 0:
                    firebaseFrag1(rootView);
                    break;
                case 1:
                    firebaseFrag2(rootView);
                    break;
                /*case 0:
                   // fragmentView0(rootView);
                    break;
                case 1:
                    break;
                case 2:
                    fragmentView2(rootView);
                    break;*/
            }
            return rootView;
        }

        private void firebaseFrag2(View rootView) {
        }

        /**
         * Display all current tickets(ArrayList<Ticket> from firebase)
         * @param rootView
         */
        private void firebaseFrag1(View rootView) {
           final ProgressDialog pd = ProgressDialog.show(this.employee_activity,"Updating Tickets","Please Wait...",true,false);
            pd.show();
            ListView lv = (ListView) rootView.findViewById(R.id.mobile_tickets_lv);
            //make call to firebase
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            db.getReference().orderByChild("store_number").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Store s = ds.getValue(Store.class);
                        if(s.getPhone().equalsIgnoreCase(employee_activity.getStoreID()) && s.getStylistHashMap().containsKey(employee_activity.getStylistID())){
                          employee_activity.setStore(s);
                            //LIST ADAPTER FOR TICKETS
                            displayTickets(employee_activity,pd);
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                }
            });
        }

        /**
         * This method gets called after it queries firebase..
         * @param employee_activity
         */
        private void displayTickets(final EmployeeActivity employee_activity, final ProgressDialog pd) {
            ArrayList<Ticket> list = new ArrayList<>(employee_activity.getStore().getTickets());
            //Log.e("DISPLAYING TICKETS: ", list.toString());
            this.employee_activity =employee_activity;
            ListView lv = (ListView)this.employee_activity.findViewById(R.id.mobile_tickets_lv);
            ListViewTicketAdapter la = new ListViewTicketAdapter(this.employee_activity,list);
            lv.setAdapter(la);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    Ticket t = (Ticket) adapterView.getAdapter().getItem(pos);//((ListViewTicketAdapter)lv.getAdapter()).getItem(pos);
                    Toast.makeText(employee_activity,t.getUnique_id()+" selected",Toast.LENGTH_LONG).show();
                    showAlertDialog(t);
                }
            });
            pd.dismiss();
        }

        private void showAlertDialog(final Ticket t) {
            new AlertDialog.Builder(this.employee_activity).setTitle("Remove Ticket #"+t.getUnique_id()).setMessage("Are you sure you want to delete " +
                    "Ticket:[#"+t.getUnique_id()+" name: "+t.getName()+"] ?").setIcon(this.employee_activity.getResources().getDrawable(android.R.drawable.ic_dialog_alert))
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteTicket(t);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

        /**
         * Make a transaction to delete Ticket
         * @param t
         */
        private void deleteTicket(final Ticket t){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(String.valueOf(this.employee_activity.store.getStore_number())).child("tickets");
            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    if(mutableData.getValue() == null){
                      return Transaction.success(mutableData);
                    }
                    GenericTypeIndicator<List<Ticket>>gti = new GenericTypeIndicator<List<Ticket>>() {};
                    List<Ticket> list = mutableData.getValue(gti);
                    list.remove(t);
                    mutableData.setValue(list);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        Toast.makeText(employee_activity,"Ticket successfully deleted!",Toast.LENGTH_SHORT).show();
                }
            });
           ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()==null)return;
                    //Log.e("DATASNAP: ",dataSnapshot.toString());
                    GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {};
                    List<Ticket> list = dataSnapshot.getValue(gti);
                    ArrayList<Ticket> tickets = new ArrayList<Ticket>(list);
                    ListView lv = (ListView)employee_activity.findViewById(R.id.mobile_tickets_lv);
                    lv.setAdapter(null);
                    ListViewTicketAdapter la = new ListViewTicketAdapter(employee_activity,tickets);
                    lv.setAdapter(la);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                }
            });

        }


       /* private void fragmentView2(View rootView) {
            ImageView iv=(ImageView) rootView.findViewById(R.id.imageView_employee_activity);
            iv.setImageBitmap(Utils.convertBytesToBitmap(Utils.convertToByteArray(stylist.getImage_bytes())));
            TextView tv=(TextView) rootView.findViewById(R.id.textView_tab3_employee_activity);
            tv.setText(tv.getText() + stylist.getName().toUpperCase());
            Button b=(Button) rootView.findViewById(R.id.button_upload_profile_pic);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageGallery();
                }
            });
        }*/

        private void fragmentView0(View rootView) {
            TextView tv=(TextView) rootView.findViewById(R.id.textView_stylist_today_upcoming_header);
            SimpleDateFormat sdf=new SimpleDateFormat("MMM/dd/yyyy");
            tv.setText(tv.getText().toString() + sdf.format(new Date()));
            ExpandableListView lv=(ExpandableListView) rootView.findViewById(R.id.expandable_List_view);
            if(reservation != null && lva != null) {
                ProgressBar pb=(ProgressBar) rootView.findViewById(R.id.progressBar_emp_act);
                pb.setVisibility(View.GONE);
                lv.setAdapter(lva);
            } else {
                StylistWebTaskAppointments swt=new StylistWebTaskAppointments(this.employee_activity,rootView, lv);
                swt.execute();
            }
        }

        @Override
        public void setEmployeeActivityData(EmployeeActivity ea) {
            this.employee_activity = ea;
        }

        public EmployeeActivity getEmployee_activity() {
            return this.employee_activity;
        }
    }

    private void setStore(Store s) {
        this.store = s;
    }

    /**
     * A {@link SectionsPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter  {
      //  private final Bundle data;
        private EmployeeActivity ea;
        public SectionsPagerAdapter(FragmentManager fragmentManager,EmployeeActivity ea){// Bundle data) {
            super(fragmentManager);
            map = new HashMap<>();
            this.ea = ea;
           // this.data = data;
        }
        public HashMap<Integer,PlaceholderFragment> map;
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    PlaceholderFragment f = PlaceholderFragment.newInstance(position+1);
                    f.setEmployeeActivityData(ea);
                    //f.setArguments(data);
                    map.put(position,f);
                    return f;
                case  1:
                    PlaceholderFragment f2 = PlaceholderFragment.newInstance(position+1);
                    f2.setEmployeeActivityData(ea);
                    //f2.setArguments(data);
                    map.put(position,f2);
                    return f2;
                default:return null;
            }
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
           // return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
           // return 3;
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                /*case 0:
                    return "Today's Appointments";
                case 1:
                    return "Upcoming Appointments";
                case 2:
                    return "User Settings";*/
                case 0:
                    return "Current Tickets";
                case 1:
                    return "User Settings";
            }
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.e("On Request Permission","RequestCode: "+requestCode);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        galleryIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public  void openImageGallery() {
        galleryIntent();
        if(Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if(ContextCompat.checkSelfPermission(EmployeeActivity.this.getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if(ActivityCompat.shouldShowRequestPermissionRationale(EmployeeActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showReason();
                } else {
                    ActivityCompat.requestPermissions(EmployeeActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            }else{
                galleryIntent();
            }
        } else {
            galleryIntent();
        }
    }

    private  void galleryIntent() {
        Intent intent=new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        EmployeeActivity.this.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    private  void showReason() {
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(EmployeeActivity.this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("Needed to upload profile picture.");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(EmployeeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        });
        AlertDialog alert=alertBuilder.create();
        alert.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = (ImageView) EmployeeActivity.this.findViewById(R.id.imageView_employee_activity);
                imageView.setImageBitmap(bitmap);
                UploadImageWebTask u = new UploadImageWebTask(EmployeeActivity.this,bitmap);
                u.execute();
               // stylist.setBitmap(bitmap);
               // stylist.setImage_bytes(Utils.convertToString(Utils.convertBitmapToByteArray(bitmap)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public  void onBackPressed(){
        return;
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {


    }
    public String getStoreID(){
        return this.store_id;
    }
    public String getStylistID(){
        return this.stylist_id;
    }
public interface EmployeeActivityData{
    void setEmployeeActivityData(EmployeeActivity ea);
    EmployeeActivity getEmployee_activity();
}
    public static class ListViewTicketAdapter extends ArrayAdapter<Ticket> {
        private ArrayList<Ticket> list;
        public ListViewTicketAdapter(Context c, ArrayList<Ticket> values) {
            super(c, R.layout.mobile_ticket_employee_layout, values);
            this.list = values;
        }

        @Override
        public int getCount(){
            return this.list.size();
        }
        /**
         * Implement getView method for customizing row of list view.
         * this method creates store_list single view that correponds to the data being passed in.
         * get the STORE data by getItem(position)
         */
        public View getView(final int position_item, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            View rowView = inflater.inflate(R.layout.mobile_ticket_employee_layout, parent, false);
            Ticket t = this.getItem(position_item);
            TextView name = (TextView)rowView.findViewById(R.id.cust_name_tv);
            name.setText(t.getName());
            TextView tn = (TextView)rowView.findViewById(R.id.ticket_number_tv);
            tn.setText(String.valueOf(t.getUnique_id()));
            return rowView;
        }
    }

}

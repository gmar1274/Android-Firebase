package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.App_Objects.MyIntent;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Recycleview.RVAdapter;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;
import app.reservation.acbasoftare.com.reservation.WebTasks.LockDBPreserveSpot;
import app.reservation.acbasoftare.com.reservation.WebTasks.SalonServiceWebTask;
import app.reservation.acbasoftare.com.reservation.WebTasks.StoresWebTask;
import app.reservation.acbasoftare.com.reservation.WebTasks.StylistWebTask;

//import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {
    public static int ticket_number;///is the potential ticket number
    public static ArrayList<Store> store_list = null;
    public static ArrayList<Stylist> stylists_list = null;
    public static ListView lv = null;
    public static ListAdapter la = null;
    public static View rootView = null, mainView = null;
    public static int stylist_postion = 0;
    public static Activity a;
    public static TabLayout tabLayout;
    public static Location user_loc;
    public static int miles = 10;
    public static GoogleMap gm;
    public static MapView mv;
    public static int selectedPosition;
    public static Ticket TICKET;
    public static String phone;//user phone number
    public static boolean isSuccess;//success payment ticket register
    public static View rootView_LiveTab, rootView_Reservation;
    // public static Appointment appointment;//used to hold data in third tab...
    //public static SwipeRefreshLayout srl;
    public static Profile user_fb_profile;
    public static MyIntent myIntent;
    public static RecyclerView recyclerView_stylists;
    public static boolean noStaff;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static AlphaAnimation inAnimation;
    private static RecyclerView recyclerView_services;
    /**
     * The {@link } that will provide
     * fragments for each of the sections. We use store_list
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to store_list
     * {@link }.
     */
    public CustomFragPageAdapter mCustomFragPageAdapter;
    public ViewPager mViewPager;

    public static void showGoogleMaps(final View rootView, final ArrayList<Store> store_list) {

        mv = (MapView) rootView.findViewById(R.id.mapView);

        mv.onCreate(null);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mv.getMapAsync(new OnMapReadyCallback() {
            /** Called when the user clicks a marker. */
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gm = googleMap;
                gm.setOnMarkerClickListener(
                        new GoogleMap.OnMarkerClickListener() {
                            public boolean onMarkerClick(final Marker marker) {

                                // Retrieve the data from the marker.
                                Integer storePos = (Integer) marker.getTag();

                                // Check if a click count was set, then display the click count.
                                if (storePos != null) {
                                    if (storePos == -1) return false;//user position -1 by default
                                    // clickCount = clickCount + 1;
                                    // marker.setTag(clickCount);
                                    // Toast.makeText(MainActivity.a, marker.getTitle() + " has been clicked " + storePos,Toast.LENGTH_SHORT).show();
                                }

                                // Return false to indicate that we have not consumed the event and that we wish
                                // for the default behavior to occur (which is for the camera to move such that the
                                // marker is centered and for the marker's info window to open, if it has one).
                                return false;
                            }

                        }
                );
                gm.getUiSettings().setMyLocationButtonEnabled(false);
                if (ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(rootView.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Log.e("ERRRR,,", "ERRRRR GM permission gor google maps");
                    return;
                }
                 com.google.android.gms.maps.model.LatLng  myLoc = new com.google.android.gms.maps.model.LatLng(MainActivity.user_loc.getLatitude(), MainActivity.user_loc.getLongitude());
                gm.addMarker(new MarkerOptions()
                                     .position(myLoc)
                                     .title("My Loctaion").alpha(.5f)).setTag(-1);

                for (Store s : store_list) {
                     com.google.android.gms.maps.model.LatLng loc = new com.google.android.gms.maps.model.LatLng(s.getLocation().latitude, s.getLocation().longitude);
                    gm.addMarker(new MarkerOptions().position(loc).title(s.getName())).setTag(s.getStore_number());
                }

                // Updates the location and zoom of the MapView
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLoc, 10);//20 is closeset 5 is largest
                gm.animateCamera(cameraUpdate);
                mv.onResume();
            }

        });

        ///////////////////////////////////////////////////////////google map
    }

    /**
     * This method doesnt need to check null states of lists because this function will only get called when list>0 and not null
     * Update Calendar with appointments
     */
    public static void updateRVServices(final Store s) {
        //fill out the
        //Store s=store_list.get(selectedPosition);////get the store
        final RVAdapter<Stylist> stylist_adapter = (RVAdapter<Stylist>) recyclerView_stylists.getAdapter();

        recyclerView_services = (RecyclerView) rootView_Reservation.findViewById(R.id.rv_service_view);
        recyclerView_services.setHasFixedSize(true);
        recyclerView_services.setAdapter(new RVAdapter<SalonService>(s.getSalonServicesArrayList(), R.layout.rv_service, false));//fasle for not stylits...services==true
        LinearLayoutManager lll = new LinearLayoutManager(rootView_Reservation.getContext());
        lll.setOrientation(lll.HORIZONTAL);
        recyclerView_services.setLayoutManager(lll);
        recyclerView_services.getAdapter().notifyDataSetChanged();
        final RVAdapter<SalonService> ss_adapter = (RVAdapter<SalonService>) recyclerView_services.getAdapter();
        ////update CalendarView
        CalendarView cv = (CalendarView) rootView_Reservation.findViewById(R.id.calendarView);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H:mm a");
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                Date d = calendar.getTime();
                stylist_adapter.customeNotifyDataSetChanged();
                if (d.before(new Date()) || noStaff) return;
                startReservationActivity(d, s, stylist_adapter.getStylist(), ss_adapter.getSalonService());
                /**MainActivity.a.setContentView(R.layout.fragment_reservation);//////////////update view to fragment
                 ReservationFragment newFragment = new ReservationFragment();
                 newFragment.init(d,stylist_adapter.getStylist(),s,ss_adapter.getSalonService());
                 FragmentTransaction transaction = a.getFragmentManager().beginTransaction();
                 transaction.add(newFragment,null).commit();
                 *///////////////////////////////////////////

                // transaction.commit();//activates oncreate() from testing ive done...
            }//met
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_services.getContext(), lll.getOrientation());
        recyclerView_services.addItemDecoration(dividerItemDecoration);

    }/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////tab 3

    /**
     * Starts a new activity for a given stylist and their available time with a specified service
     *
     * @param store - Stylist and SalonService
     */
    private static void startReservationActivity(Date date, Store store, Stylist s, SalonService ss) {
        myIntent = null;
        myIntent = new MyIntent(date, store, s, ss);
        Intent intent = new Intent(MainActivity.a, ReservationActivity.class);
        // intent.putExtra("store",store);
        //intent.putExtra("stylist",s);
        // intent.putExtra("service",ss);
        MainActivity.a.startActivity(intent);
    }

    private static void showCreditCard() {
        if (MainActivity.stylists_list == null || MainActivity.store_list == null || MainActivity.store_list.size() == 0)
            return;
        LockDBPreserveSpot db = new LockDBPreserveSpot();
        db.execute(store_list.get(selectedPosition).getPhone());
        CreditCardDialog ccd = new CreditCardDialog(MainActivity.selectedPosition, MainActivity.stylist_postion, phone);
        ccd.showCreditCardDialog();
    }

    /**
     * Reset the RV for this tab
     */
    public static void updateTab3() {
        if (MainActivity.recyclerView_stylists != null) {//delete stylists
            MainActivity.recyclerView_stylists.setAdapter(null);
        }
        if (MainActivity.recyclerView_services != null) {
            MainActivity.recyclerView_services.setAdapter(null);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected", selectedPosition);
        outState.putParcelableArrayList("store_list", store_list);
        outState.putParcelable("ticket", TICKET); // mv.onSaveInstanceState(outState);
        outState.putInt("ticket_number", ticket_number);
        outState.putBoolean("issSuccess", isSuccess);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // LoginActivity.debugDisplayGPS(this);
        user_loc = LoginActivity.gps.getLocation();
        mv = null;
        gm = null;
        la = null;
        lv = null;
        askUserPermissionForPhone();
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt("selected");
            store_list = savedInstanceState.getParcelableArrayList("store_list");
            TICKET = savedInstanceState.getParcelable("ticket");
            ticket_number = savedInstanceState.getInt("ticket_number");
            isSuccess = savedInstanceState.getBoolean("isSuccess");
        } else {
            selectedPosition = 0;//initial pos
            ticket_number = -1;//not set
            isSuccess = false;
            store_list = null;
            stylists_list = null;
            stylist_postion = 0;
            noStaff = true;
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return store_list fragment for each of the three
        // primary sections of the activity.
        mCustomFragPageAdapter = new CustomFragPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCustomFragPageAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (stylists_list != null && stylists_list.size() > 0) {
                    noStaff = false;
                } else noStaff = true;
                if (position == 0) {
                    //Appointment.reset();
                    return;
                } else if (position == 1) {//second tab or LIVE FEED TAB//0,1,2 page numbers
                    // Appointment.reset();
                    if (MainActivity.store_list == null || store_list.size() == 0) {
                        return;
                    } else {
                        Store s = store_list.get(selectedPosition);
                        StylistWebTask swt = new StylistWebTask(rootView_LiveTab);
                        if (stylists_list == null) {
                            swt.execute(s.getPhone());
                        }
                    }
                    //mv.onCreate(savedInstanceState);
                } else if (position == 2) {//the third tab . aka Reservation Appointment
                    updateTab3();
                    if (stylists_list == null || stylists_list.size() == 0) {
                        noStaff = true;
                        return;
                    } else {
                        setUpRV3rdTab();
                        if (store_list != null && store_list.size() > 0) {
                            Store store = store_list.get(selectedPosition);
                            SalonServiceWebTask ss = new SalonServiceWebTask(rootView_Reservation, store, selectedPosition);
                            ss.execute(store.getPhone());//loads the R.V. adapter with the loaded list of Services
                        }
                    }
                }
            }

            private void setUpRV3rdTab() {
                recyclerView_stylists = (RecyclerView) rootView_Reservation.findViewById(R.id.rv_stylist_view);
                recyclerView_stylists.setHasFixedSize(true);
                recyclerView_stylists.setAdapter(new RVAdapter<Stylist>(Utils.getArrayListStylist(stylists_list), R.layout.rv_stylist, true));
                LinearLayoutManager ll = new LinearLayoutManager(rootView_Reservation.getContext());
                ll.setOrientation(ll.HORIZONTAL);
                recyclerView_stylists.setLayoutManager(ll);
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_stylists.getContext(), ll.getOrientation());
                recyclerView_stylists.addItemDecoration(dividerItemDecoration);
                recyclerView_stylists.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        MainActivity.a = this;
    }

    /**
     * This method request the OS for permission to grab a phone number
     * If this is a tablet then it will return null
     */
    private void askUserPermissionForPhone() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            //  Toast.makeText(this,"NOT GRANTED",Toast.LENGTH_LONG).show();
        } else {
            getPhoneNumber();//access granted

        }
    }

    /**
     * Helper method for @askUserPermissionForPhone
     * This method assumes if user doesnt have phone number then phone field is non null blank.
     * If there exist a phone number then it assumes the number is 1 followed by 10 digit number
     */
    private void getPhoneNumber() {
        try {
            TelephonyManager mManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = mManager.getLine1Number();
            if (mPhoneNumber.length() > 10) {
                phone = mPhoneNumber.substring(1, mPhoneNumber.length());//get rid of the one
            } else {
                phone = mPhoneNumber;
            }
        } catch (Exception e) {
            phone = "";
            //Toast.makeText(this,"ERROR",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoneNumber();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    getPhoneNumber();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    ////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify store_list parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            //noinspection SimplifiableIfStatement
            case R.id.account_settings:
                return true;
            case R.id.credit_card_settings:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////for ggogle maps
    //@Override
    public void onResume() {
        super.onResume();
        if (mv != null)
            mv.onResume();
    }

    //@Override
    public void onPause() {
        super.onPause();
        //need to update the DAte value check wheteher is old or current for user
        if (ticket_number >= 0 && isSuccess == false) {///if the user goes to credit card dialog then shuts the app off
            //need to revoke his ticket
            LockDBPreserveSpot delete = new LockDBPreserveSpot();
            delete.execute(store_list.get(selectedPosition).getPhone(), ticket_number + "");///remove ticket
            //takes only 2 params, store and ticketnumber
        }
        if (mv != null) mv.onPause();
    }

    //  @Override
    public void onDestroy() {
        super.onDestroy();
        if (mv != null) mv.onDestroy();
    }

    //@Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mv != null)
            mv.onLowMemory();
    }

    public void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        if (mv != null) mv.onStop();

    }

    public void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.

        if (mv != null)
            mv.onStart();

    }

    public final void onExitAmbient() {
        if (mv != null)
            mv.onExitAmbient();
    }

    public final void onEnterAmbient(Bundle ambientDetails) {
        if (mv != null) mv.onEnterAmbient(ambientDetails);
    }

    ///////////////////////////end google maps
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "portrait", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Update the store in the list with the parameter store
     * @param store
     */
    public static void updateStore(Store store) {
        MainActivity.store_list.remove(selectedPosition);
        MainActivity.store_list.add(selectedPosition,store);
    }

    /**
     * A placeholder fragment containing store_list simple view.
     */
    public static class TabFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public TabFragment() {

        }

        /**
         * Returns store_list new instance of this fragment for the given section
         * number.
         */
        public static TabFragment newInstance(int sectionNumber) {

            TabFragment fragment = new TabFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /*This method displays the individual tabs when swiping left or right
        * */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //that holds all components(Views) for the tab screens
            int page = this.getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (page - 1) {
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);///this is the fragment view
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_layout_live_feed, container, false);
                    rootView_LiveTab = rootView;
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.layout_appointment, container, false);
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
                    fragmentView0(rootView);
                    break;
                case 1:
                    fragmentView1(rootView);
                    break;
                case 2:
                    fragmentView2(rootView);
                    break;
            }
            return rootView;
        }

        private void fragmentView2(View rootView) {
            MainActivity.rootView_Reservation = rootView;
        }

        private void fragmentView0(final View rootView) {
            MainActivity.rootView = rootView;
            final TextView miles = (TextView) rootView.findViewById(R.id.textView_distance);
            final SeekBar sb = (SeekBar) rootView.findViewById(R.id.seekBar_radius);
            sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    miles.setText((progress + 1) + " mi");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            //MainActivity.miles = sb.getProgress() + 1;
/*
 * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
 * performs a swipe-to-refresh gesture.
 */
            /** srl = ((SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout));
             srl.setOnRefreshListener(
             new SwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
            if (MainActivity.miles==0 || MainActivity.miles==10) {//deafult dont refresh unless changed
            srl.setRefreshing(false);
            return;//default is 10 miles
            }
            // Log.i("REFRESH CALLED", "onRefresh called from SwipeRefreshLayout");

            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            // myUpdateOperation();
            //Toast.makeText(rootView.getContext(), "REFRESH CALLED", Toast.LENGTH_LONG).show();
            //srl.setRefreshing(false);//need to set the icon off
            store_list = null;
            lv = null;
            la = null;
            StoresWebTaskPopulateStoreFromOldMYSQLServer swt = new StoresWebTaskPopulateStoreFromOldMYSQLServer(rootView);
            swt.execute(sb.getProgress()+"");
            }
            }
             );**/
            Button search = (Button) rootView.findViewById(R.id.button_search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainActivity.miles == sb.getProgress() + 1) {//repeated
                        return;
                    } else {//get new search results
                        store_list = null;
                        stylists_list = null;
                        lv = null;
                        la = null;
                        StoresWebTask swt = new StoresWebTask(rootView);
                        swt.execute(sb.getProgress() + "");
                    }
                }
            });
            if (MainActivity.store_list == null) {//first time running
                StoresWebTask swt = new StoresWebTask(rootView);
                swt.execute(sb.getProgress() + "");
            } else {///could be here because of screen orient change so update ui
                MainActivity.lv = (ListView) rootView.findViewById(R.id.fragment_listview);
                la = new ListViewAdapter(rootView.getContext(), store_list);
                lv.setAdapter(la);
                showGoogleMaps(rootView, store_list);
            }
        }

        /**
         * This is the View to be inflated for the 2 tab or (tab #=1). Stylist View
         * This method seems to only get called when begin of app. TTHUS it really doesnt function unless
         * on tab listener........
         */
        private void fragmentView1(View rootView) {
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Make Reservation", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    showCreditCard();

                }
            });
        }
    }
    /////////////////////////////////////////////////////////////////////////////////END CLASS

    /**
     * This is for Store list
     */
    public static class ListViewAdapter extends ArrayAdapter<Store> {
        public ListViewAdapter(Context c, ArrayList<Store> values) {
            super(c, R.layout.list_view_layout, values);
        }

        /**
         * Implement getView method for customizing row of list view.
         * this method creates store_list single view that correponds to the data being passed in.
         * get the STORE data by getItem(position)
         */
        public View getView(final int position_item, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            // Creating store_list view of row.
            View rowView = inflater.inflate(R.layout.list_view_layout, parent, false);
            RadioButton r = (RadioButton) rowView.findViewById(R.id.radio_button);
            double miles_away = getItem(position_item).getMiles_away();
            DecimalFormat df = new DecimalFormat("0.##");
            r.setText("Store: " + getItem(position_item) + "\n" + "Miles away: " + df.format(miles_away) + "\n" + "Address: " + getItem(position_item).getAddress().toUpperCase() + "\n" + getItem(position_item).getCitystate().toUpperCase());

            r.setChecked(position_item == selectedPosition);
            r.setTag(position_item);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position_item != selectedPosition) {//new store...go get the stylists from that db
                        stylists_list = null;//new store null stylists
                    }
                    selectedPosition = (Integer) view.getTag();

                    notifyDataSetChanged();//updates the button click isset
                    LatLng myLoc = getItem(position_item).getLocation();
                    // Updates the location and zoom of the MapView
                     com.google.android.gms.maps.model.LatLng l = new com.google.android.gms.maps.model.LatLng(myLoc.latitude,myLoc.longitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(l, 14);//20 is closeset 5 is largest
                    gm.animateCamera(cameraUpdate);
                    mv.onResume();
                }
            });
            return rowView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns store_list fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class CustomFragPageAdapter extends FragmentPagerAdapter {
        public CustomFragPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return store_list TabFragment (defined as store_list static inner class above).
            return TabFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Store Location";
                case 1:
                    return "Live Feed";
                case 2:
                    return "Make Reservation";
            }
            return null;
        }
    }
}
package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Invoice;
import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;
import app.reservation.acbasoftare.com.reservation.App_Objects.MyIntent;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;
import app.reservation.acbasoftare.com.reservation.App_Services.GPSLocation;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.FirebaseWebTasks.FirebaseWebTasks;
import app.reservation.acbasoftare.com.reservation.ListAdapters.UserMessageMetaDataAdapter;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Recycleview.RVAdapter;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static android.R.attr.data;
import static android.R.attr.key;
import static android.os.Build.VERSION_CODES.M;
import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.AD_AGE_DATE_STRING;
import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.sdf;
import static app.reservation.acbasoftare.com.reservation.R.id.map;
import static app.reservation.acbasoftare.com.reservation.Utils.Utils.testPeriodMap;
import static com.google.api.client.http.HttpMethods.HEAD;

//import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    public final static boolean ADTESTING = false;//false means LIVE ads
    public static final long IMAGE_DOWNLOAD_LIMIT = 1024 * 1024 * 10 ;
    public static HashMap<String, Bitmap> stylist_bitmaps;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static AlphaAnimation inAnimation;
    //public static int ticket_number;///is the potential ticket number
    public ArrayList<FirebaseStore> store_list = null;
    public ArrayList<Stylist> stylists_list = null;
    // public static ListView lv = null;
    // public static ListAdapter la = null;

    public int stylist_position = 0;
    public TabLayout tabLayout;
    public Location user_loc;
    public int miles = 10;
    public GoogleMap gm;
    public MapView mv;
    public int selectedPosition;
    public FirebaseStore store;
    //public static Ticket TICKET;
    public String phone;//user phone number
    public boolean isSuccess;//success payment ticket register
    public boolean STYLIST_BITMAPS_LOADED;

    public Profile user_fb_profile;
    public MyIntent myIntent;

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
    public ArrayList<Ticket> ticket_history;
    //private long current_ticket;
    private HashMap<String, Stylist> sty_hm;
    private boolean noStaff;
    private PublisherInterstitialAd mPublisherInterstitialAd;

    public void showGoogleMaps(final View rootView, final ArrayList<FirebaseStore> store_list) {

        if (mv == null)
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
                                Long storePos = (Long) marker.getTag();

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
                com.google.android.gms.maps.model.LatLng myLoc = new com.google.android.gms.maps.model.LatLng(MainActivity.this.user_loc.getLatitude(), MainActivity.this.user_loc.getLongitude());
                gm.addMarker(new MarkerOptions()

                        .position(myLoc)
                        .title("My Loctaion").alpha(.5f)).setTag(-1);


                for (FirebaseStore s : store_list) {
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
    public void updateRVServices(final Store s) {
        //fill out the
        //Store s=store_list.get(selectedPosition);////get the store
       /* final RVAdapter<Stylist> stylist_adapter = (RVAdapter<Stylist>) recyclerView_stylists.getAdapter();

        RecyclerView   recyclerView_services = (RecyclerView) mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getView().findViewById(R.id.rv_service_view);
        recyclerView_services.setHasFixedSize(true);
        recyclerView_services.setAdapter(new RVAdapter<SalonService>(this, s.getSalonServicesArrayList(), R.layout.rv_service, false));//fasle for not stylits...services==true
        LinearLayoutManager lll = new LinearLayoutManager(mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getContext());
        lll.setOrientation(lll.HORIZONTAL);
        recyclerView_services.setLayoutManager(lll);
        recyclerView_services.getAdapter().notifyDataSetChanged();
        final RVAdapter<SalonService> ss_adapter = (RVAdapter<SalonService>) recyclerView_services.getAdapter();
        ////update CalendarView
        CalendarView cv = (CalendarView) mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getView().findViewById(R.id.calendarView);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy H:mm a");
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                Date d = calendar.getTime();
                stylist_adapter.customeNotifyDataSetChanged();
                if (d.before(new Date()) ) return;
                startReservationActivity(d, s, stylist_adapter.getStylist(), ss_adapter.getSalonService());
                *//**MainActivity.a.setContentView(R.layout.fragment_reservation);//////////////update view to fragment

         ReservationFragment newFragment = new ReservationFragment();
         newFragment.init(d,stylist_adapter.getStylist(),s,ss_adapter.getSalonService());
         FragmentTransaction transaction = a.getFragmentManager().beginTransaction();
         transaction.add(newFragment,null).commit();
         *//*//////////////////////////////////////////
>>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a

                // transaction.commit();//activates oncreate() from testing ive done...
            }//met
        });
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView_services.getContext(), lll.getOrientation());
        recyclerView_services.addItemDecoration(dividerItemDecoration);*/

    }/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////tab 3

    /**
     * Starts a new activity for a given stylist and their available time with a specified service
     *
     * @param store - Stylist and SalonService
     */
    private void startReservationActivity(Date date, Store store, Stylist s, SalonService ss) {
        myIntent = null;
        myIntent = new MyIntent(date, store, s, ss, this.selectedPosition);
        Intent intent = new Intent(MainActivity.this, ReservationActivity.class);
        // intent.putExtra("store",store);
        //intent.putExtra("stylist",s);
        // intent.putExtra("service",ss);
        MainActivity.this.startActivity(intent);
    }

    private void showCreditCard() {
        if (stylists_list == null || store_list == null || store_list.size() == 0)
            return;
        // LockDBPreserveSpot db = new LockDBPreserveSpot();
        //db.execute(store_list.get(selectedPosition).getPhone());
        CreditCardDialog ccd = new CreditCardDialog(MainActivity.this, selectedPosition, stylist_position, phone);
        ccd.showCreditCardDialog();
    }

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        return;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selected", selectedPosition);
        outState.putParcelableArrayList("store_list", store_list);
        //  outState.putParcelable("ticket", TICKET); // mv.onSaveInstanceState(outState);
        //outState.putInt("ticket_number", ticket_number);
        outState.putParcelableArrayList("ticket_history", ticket_history);
        outState.putBoolean("issSuccess", isSuccess);
        outState.putSerializable("stylist_bitmaps", stylist_bitmaps);
        outState.putBoolean("STYLIST_BITMAPS_LOADED", STYLIST_BITMAPS_LOADED);
        outState.putSerializable("sty_hm", sty_hm);
        outState.putParcelable("store", store);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // LoginActivity.debugDisplayGPS(this);
        GPSLocation gps = this.getIntent().getParcelableExtra("gps");
        user_loc = gps.getLocation();
        mv = null;
        gm = null;
        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId("ca-app-pub-9309556355508377/7384464845");
        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });
        requestNewInterstitial();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9309556355508377~8508953646");


        askUserPermissionForPhone();
        if (savedInstanceState != null) {
            selectedPosition = savedInstanceState.getInt("selected");
            store_list = savedInstanceState.getParcelableArrayList("store_list");
            //TICKET = savedInstanceState.getParcelable("ticket");
            //ticket_number = savedInstanceState.getInt("ticket_number");
            isSuccess = savedInstanceState.getBoolean("isSuccess");
            stylist_bitmaps = (HashMap<String, Bitmap>) savedInstanceState.getSerializable("stylist_bitmaps"); //getParcelableArrayList("stylist_bitmaps");
            STYLIST_BITMAPS_LOADED = savedInstanceState.getBoolean("STYLIST_BITMAPS_LOADED");
            ticket_history = savedInstanceState.getParcelableArrayList("ticket_history");
            sty_hm = (HashMap<String, Stylist>) savedInstanceState.getSerializable("sty_hm");
            store = savedInstanceState.getParcelable("store");
        } else {
            selectedPosition = 0;//initial pos
            //ticket_number = -1;//not set
            isSuccess = false;
            store_list = null;
            stylists_list = null;
            stylist_position = 0;
            noStaff = true;
            stylist_bitmaps = new HashMap<>();
            STYLIST_BITMAPS_LOADED = false;
            ticket_history = new ArrayList<>();
            sty_hm = new HashMap<>();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.user_fb_profile= this.getIntent().getParcelableExtra("fb_profile");

        // Create the adapter that will return store_list fragment for each of the three
        // primary sections of the activity.
        mCustomFragPageAdapter = new CustomFragPageAdapter(getSupportFragmentManager(), MainActivity.this);

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
                if (position == 0) {////////////FIRST STORE VIEW TAB NOTHING TO DO
                    //Appointment.reset();
                    return;
                } else if (position == 1) {//second tab or LIVE FEED TAB//0,1,2 page numbers
                    AdView mAdView = (AdView) MainActivity.this.getCurrentFragmentDisplayFromMainAct().getView().findViewById(R.id.adView_liveFeed);
                    AdRequest adRequest = null;

                    if (ADTESTING) {
                        adRequest = new AdRequest.Builder().addTestDevice("23B075DED4F5E3DB63757F55444BFF46").build();
                    } else {
                        //adRequest = new AdRequest.Builder().build(); //addTestDevice("23B075DED4F5E3DB63757F55444BFF46").build();

                        try {
                            adRequest = new AdRequest.Builder().setBirthday(sdf.parse(AD_AGE_DATE_STRING))
                                    .build();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    mAdView.loadAd(adRequest);

                    if (stylist_bitmaps != null) {
                        stylist_bitmaps.clear();
                        stylist_bitmaps = null;
                        stylist_bitmaps = new HashMap<String, Bitmap>();
                    } else {
                        stylist_bitmaps = new HashMap<String, Bitmap>();
                    }
                    if (stylists_list != null) {
                        stylists_list.clear();
                        stylists_list = null;
                        stylists_list = new ArrayList<Stylist>();
                    } else {
                        stylists_list = new ArrayList<Stylist>();
                    }
                    FirebaseStore s = store_list.get(selectedPosition);
                    store = s;
                    loadFirebaseStylist(s);

                    //mv.onCreate(savedInstanceState);
                } /*else if (position == 2) {//the third tab . aka Reservation Appointment
                    updateTab3();
                    if (stylists_list == null || stylists_list.size() == 0) {
                        noStaff = true;
                        return;
                    } else {
                        setUpRV3rdTab();
                        if (store_list != null && store_list.size() > 0) {
                            Store store = store_list.get(selectedPosition);
                            SalonServiceWebTask ss = new SalonServiceWebTask(MainActivity.this,rootView_Reservation, store, selectedPosition);
                            ss.execute(store.getPhone());//loads the R.V. adapter with the loaded list of Services
                        }
                    }
                }*/
            }

            private void setUpRV3rdTab() {

                RecyclerView recyclerView_stylists = (RecyclerView) mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getView().findViewById(R.id.rv_stylist_view);

                recyclerView_stylists.setHasFixedSize(true);
                recyclerView_stylists.setAdapter(new RVAdapter<Stylist>(MainActivity.this, Utils.getArrayListStylist(stylists_list), R.layout.rv_stylist, true));
                LinearLayoutManager ll = new LinearLayoutManager(mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getContext());
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
        // MainActivity.a = this;
    }

    /**
     * Fetch the stylist pictures from the database.
     */
    private void loadFirebaseStylist(final FirebaseStore store) {
        if (store == null) return;

        final ProgressDialog pd = ProgressDialog.show(this, "Searching", "Please Wait...", true, false);
        pd.show();

        if (sty_hm == null) {
            this.sty_hm = new HashMap<>();
        } else {
            this.sty_hm.clear();
        }
        if (stylists_list == null) {
            stylists_list = new ArrayList<>();
        } else {
            this.stylists_list.clear(); //store_list.clear();
        }
        if (stylist_bitmaps != null) {
            stylist_bitmaps.clear();
        } else {
            stylist_bitmaps = new HashMap<String, Bitmap>();
        }
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("stylists/" + String.valueOf(store.getStore_number()));
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e("STORE_NUMBER IN:","IN stylists "+store.getStore_number());
                if (dataSnapshot.getValue() == null) {
                    ListView lv = (ListView) mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getView().findViewById(R.id.fragment_livefeed_listview);
                    lv.setAdapter(null);
                    pd.dismiss();
                    return;
                }

                GenericTypeIndicator<Map<String, Stylist>> gti = new GenericTypeIndicator<Map<String, Stylist>>() {
                };
                Map<String, Stylist> map = dataSnapshot.getValue(gti);
                sty_hm = new HashMap<String, Stylist>(map);

                stylists_list = new ArrayList<Stylist>(sty_hm.values());
                Collections.sort(stylists_list);
                //got the stylists now need to get images
                for (final Stylist sty : stylists_list) {
                    StorageReference sr = FirebaseStorage.getInstance().getReference().child(store.getPhone() + "/images/stylists/" + sty.getId());
                    sr.getBytes(IMAGE_DOWNLOAD_LIMIT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            Log.e("image success..", "loaded success..");
                            stylist_bitmaps.put(sty.getId(), Utils.convertBytesToBitmap(bytes));
                            if (stylists_list.size() == stylist_bitmaps.size()) {
                                Log.e("all finished.", "loaded all of pics...");
                                pd.dismiss();
                                predictTicketWait(store, stylists_list); //done with loading images
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override

                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
            }
        });//end getting data from firebase

        DatabaseReference r = FirebaseDatabase.getInstance().getReference().child("user/" + store.getStore_number() + "/current_ticket");
        r.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    store.setCurrent_ticket(1);
                } else {
                    long ct = (long) dataSnapshot.getValue();
                    store.setCurrent_ticket(ct);
                }
                TextView tv = (TextView) MainActivity.this.getCurrentFragmentDisplayFromMainAct().getView().findViewById(R.id.currentTicketTextView);
                tv.setText(String.valueOf(store.getCurrent_ticket()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Fragment getCurrentFragmentDisplayFromMainAct() {
        return this.mCustomFragPageAdapter.getCurrentFragmentView(this.mViewPager.getCurrentItem());
    }

    /***
     * THIS METHOD RUNS AFTER ALL STYLISTS AND BITMAPS ARE READY TO DISPLAY
     * <<<<<<< HEAD
     * <p>
     * =======
     * >>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
     *
     * @param store
     * @param stylists_list
     */
    private void predictTicketWait(final FirebaseStore store, final ArrayList<Stylist> stylists_list) {
        FirebaseDatabase.getInstance().getReference().child("tickets/" + store.getStore_number()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                zeroOutStylsts(stylists_list);//reset wait to zero
                if (dataSnapshot.getValue() == null) {

                    initializeStylists(stylists_list, stylist_bitmaps);
                    return;
                }
                GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {
                };

//<<<<<<< HEAD
                try {
                    List<Ticket> list = dataSnapshot.getValue(gti);

                    if (list.size() == 0) {
                        initializeStylists(stylists_list, stylist_bitmaps);
                        return;
                    }
                    Ticket lastKnownTicket = list.get(list.size() - 1);
                    TextView tv = (TextView) MainActivity.this.getCurrentFragmentDisplayFromMainAct().getView().findViewById(R.id.nextAvailableTicket_tv);
                    tv.setText(String.valueOf(lastKnownTicket.unique_id + 1));
                    /////calculate algorithm for oredictive then display
                    final Ticket user = new Ticket(lastKnownTicket);
                    HashMap<String, PriorityQueue<Ticket>> queues = generateQueues(list, stylists_list, user);//every stylists should have sorted queue

                    PriorityQueue<Ticket> no_pref = queues.get("-1");//-1 is a CONSTANT for STORE_NO_PREFERENCE STYLIST
                    queues.remove("-1");//got what we needed now delete it
                    boolean finished = false;
                    while (no_pref.size() > 0 && finished == false) {//distrubute this queue to the rest of the stylists...
                        for (String key : queues.keySet()) {
                            Ticket no_p = no_pref.poll();
                            if (no_p == null) {
                                finished = true;
                                break;
                            } else {
                                PriorityQueue<Ticket> pq = queues.get(key);
                                pq.add(no_p);
                                queues.put(key, pq);//update new queue
                            }
                        }
                    }//at the end we should have sorted queues for each stylists
                    int lowest_wait = Integer.MAX_VALUE;//used for no_preference to display approx wait time
                    for (String key : queues.keySet()) {
                        PriorityQueue<Ticket> pq = queues.get(key);
                        Object[] o = pq.toArray();
                        List<Ticket> relative = Arrays.asList(Arrays.copyOf(o, o.length, Ticket[].class));
                        Collections.sort(relative);
                        //Log.e("relative List:: ",relative.toString());
                        int index = relative.indexOf(user);//is the wait for stylist
                        Stylist temp = new Stylist(key);
                        int sty_index = stylists_list.indexOf(temp);
                        Stylist sty = stylists_list.get(sty_index);
                        sty.setPsuedo_wait(index);////////////wait if user decides to chosse this stylist
                        stylists_list.remove(sty_index);//update sty obbject
                        stylists_list.add(sty_index, sty);
                        if (sty.getPsuedo_wait() < lowest_wait) {
                            lowest_wait = sty.getPsuedo_wait();
                        }
                    }
                    Stylist s = stylists_list.get(0);/////get the store stylist
                    s.setPsuedo_wait(lowest_wait);/////set the wait that is the lowest out of all stylists
                    stylists_list.remove(0);//udpdate stylistlist
                    stylists_list.add(0, s);
                    sty_hm.put(s.getId(), s);
                    initializeStylists(stylists_list, stylist_bitmaps);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // FirebaseWebTasks.ListViewAdpaterStylist a = new FirebaseWebTasks.ListViewAdpaterStylist(MainActivity.this,0,stylists_list);
//=======
               /* try {
                    List<Ticket> list = dataSnapshot.getValue(gti);

                    if (list.size() == 0) {
                        initializeStylists(stylists_list, stylist_bitmaps);
                        return;
                    }
                    Ticket lastKnownTicket = list.get(list.size() - 1);
                    TextView tv = (TextView) MainActivity.this.getCurrentFragmentDisplayFromMainAct().getView().findViewById(R.id.nextAvailableTicket_tv);
                    tv.setText(String.valueOf(lastKnownTicket.unique_id + 1));
                    /////calculate algorithm for oredictive then display
                    final Ticket user = new Ticket(lastKnownTicket);
                    HashMap<String, PriorityQueue<Ticket>> queues = generateQueues(list, stylists_list, user);//every stylists should have sorted queue

                    PriorityQueue<Ticket> no_pref = queues.get("-1");//-1 is a CONSTANT for STORE_NO_PREFERENCE STYLIST
                    queues.remove("-1");//got what we needed now delete it
                    boolean finished = false;
                    while (no_pref.size() > 0 && finished == false) {//distrubute this queue to the rest of the stylists...
                        for (String key : queues.keySet()) {
                            Ticket no_p = no_pref.poll();
                            if (no_p == null) {
                                finished = true;
                                break;
                            } else {
                                PriorityQueue<Ticket> pq = queues.get(key);
                                pq.add(no_p);
                                queues.put(key, pq);//update new queue
                            }
                        }
                    }//at the end we should have sorted queues for each stylists
                    int lowest_wait = Integer.MAX_VALUE;
                    for (String key : queues.keySet()) {
                        PriorityQueue<Ticket> pq = queues.get(key);
                        Object[] o = pq.toArray();
                        List<Ticket> relative = Arrays.asList(Arrays.copyOf(o, o.length, Ticket[].class));
                        Collections.sort(relative);
                        //Log.e("relative List:: ",relative.toString());
                        int index = relative.indexOf(user);//is the wait for stylist
                        Stylist temp = new Stylist(key);
                        int sty_index = stylists_list.indexOf(temp);
                        Stylist sty = stylists_list.get(sty_index);
                        sty.setPsuedo_wait(index);
                        //sty.setWait(relative.size()-1);
                        stylists_list.remove(sty_index);
                        stylists_list.add(sty_index, sty);
                        if (sty.getPsuedo_wait() < lowest_wait) {
                            lowest_wait = sty.getPsuedo_wait();
                        }
                    }
                    Stylist s = stylists_list.get(0);
                    s.setPsuedo_wait(lowest_wait);
                    stylists_list.remove(0);
                    stylists_list.add(0, s);
                    sty_hm.put(s.getId(), s);
                    initializeStylists(stylists_list, stylist_bitmaps);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                initializeStylists(stylists_list, stylist_bitmaps);
            }
        });


    }

    private void zeroOutStylsts(ArrayList<Stylist> stylists_list) {
        stylists_list.clear();
        for (String id : this.sty_hm.keySet()) {
            Stylist s = this.sty_hm.get(id);
            s.setWait(0);
            this.sty_hm.put(id, s);
            stylists_list.add(s);
        }
        Collections.sort(stylists_list);
    }

    /**
     * This method will maintain a sorted quuee for each stylist. We want this to then use in preparation for our predictive algorithm
     * We then will add a POTENTIAL TICKET that will act as if the user is already inthis list
     * <<<<<<< HEAD
     * <p>
     * =======
     * >>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
     *
     * @param list
     * @param sty
     * @return
     */

    private HashMap<String, PriorityQueue<Ticket>> generateQueues(List<Ticket> list, ArrayList<Stylist> sty, Ticket user) {
        HashMap<String, PriorityQueue<Ticket>> map = new HashMap<>();
        //loop stylsits to populate all requested from the list of tickets..
        for (Stylist s : sty) {
            if(!s.isAvailable())continue;
            s.setWait(0);//zero out wait
            this.sty_hm.put(s.getId(), s);
            PriorityQueue<Ticket> pq = new PriorityQueue<>();//add the user to each stylist to simulate the poostiion would be at
            pq.add(user);
            map.put(s.getId(), pq);
        }
        for (Ticket t : list) {///////sort the tickets
            if (t != null) {
                Stylist s = this.sty_hm.get(t.stylist_id);
                s.incrementWait();
                PriorityQueue<Ticket> pq = map.get(t.stylist_id);
                pq.add(t);
                map.put(t.stylist_id, pq);//update the map
            }
        }//end sorting.
        sty = new ArrayList<>(this.sty_hm.values());
        Collections.sort(sty);
        return map;
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
            case R.id.ticket_history:
                ticketHistoryActivity();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void ticketHistoryActivity() {
        Intent i = new Intent(this, TicketHistoryActivity.class);
        // i.putExtra("ticket_number",this.ticket_number);
        // i.putExtra("stylist",this.stylist);
        i.putParcelableArrayListExtra("ticket_history", this.ticket_history);
        this.startActivity(i);
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
        // if (ticket_number >= 0 && isSuccess == false) {///if the user goes to credit card dialog then shuts the app off
        //need to revoke his ticket
        // LockDBPreserveSpot delete = new LockDBPreserveSpot();
        //delete.execute(store_list.get(selectedPosition).getPhone(), ticket_number + "");///remove ticket
        //takes only 2 params, store and ticketnumber
        //}
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

    @Override
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
     *
     * @param store
     */
    public void updateStore(FirebaseStore store) {
        MainActivity.this.store_list.remove(selectedPosition);
        MainActivity.this.store_list.add(selectedPosition, store);
    }

    /**
     * Register a transaction to firebase.
     *
     * @param store
     * @param sty
     * @param cust_name
     * @param phone
     */
    public void sendTicket(final FirebaseStore store, Stylist sty, String cust_name, String phone, final ProgressDialog pd) {

        //dont even need the first parameter because when i check the lisckets i wi;; get the last of the ticekts in thye List<Ticket>

        final Ticket t = new Ticket(store.getCurrent_ticket() + 1, (sty.getWait() + 1) + "", cust_name, sty.getId(), sty.getName(), phone);//create the ticket

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("tickets").child(String.valueOf(store.getStore_number()));
        /****
         * Make concurrent firebase call to add tickket..
         */
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() != null) {//value in this case should be list of tickets
                    GenericTypeIndicator<List<Ticket>> gti = new GenericTypeIndicator<List<Ticket>>() {
                    };
                    List<Ticket> curr_values = mutableData.getValue(gti);//get all the entries that are in this url_path

                    t.unique_id = curr_values.get(curr_values.size() - 1).unique_id + 1; //get the lastest entry plus 1

                    if (curr_values.contains(t)) {
                        t.unique_id += 1;//increment the store ticket
                        t.ticket_number = String.valueOf(Long.valueOf(t.ticket_number) + 1);//increment the next ticket waiting in line for stylist
                    }
                    curr_values.add(t);
                    mutableData.setValue(curr_values);
                } else {//there was no entries in the url so create new List<Ticket> with the first entry

                    t.unique_id = store.getCurrent_ticket() == 0 ? 1 : store.getCurrent_ticket();///first entry

                    t.ticket_number = "1";
                    List<Ticket> l = new ArrayList<Ticket>();
                    l.add(t);
                    mutableData.setValue(l);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Your ticket is #" + t.unique_id, Toast.LENGTH_LONG).show(); //resetStylistChoice();//deselect radio button
                ticket_history.add(t);
                keepTrackOfSales(t);
                if (mPublisherInterstitialAd.isLoaded()) {

                    mPublisherInterstitialAd.show();
                }
            }
        });
    }

    /**
     * <<<<<<< HEAD
     * This method will update the sale of the ticket and store it for the unique store by store_number.
     * FIREBASE: mobile-tickets/store_number/DATE/{hashMap or List of MOBILE TICKET RECIEPT..}
     *
     * @param t
     */
    private void keepTrackOfSales(Ticket t) {
        final Invoice i = new Invoice(t, store.getTicket_price());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String day = sdf.format(new Date());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("mobile-tickets-invoice/" + String.valueOf(store.getStore_number()) + "/" + day);
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                List<Invoice> list = new ArrayList<Invoice>();
                if (mutableData.getValue() == null) {//first in list
                    list.add(i);
                    mutableData.setValue(list);
                } else {
                    GenericTypeIndicator<List<Invoice>> gti = new GenericTypeIndicator<List<Invoice>>() {
                    };
                    list = mutableData.getValue(gti);
                    Collections.sort(list);
                    list.add(i);//just add at the end
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.e("Updaed invoice..", "invoice updated");
            }
        });
    }

    /**
     * =======
     * >>>>>>> 5997ae533de6ab8c38fdf6326f2cb9bdef91a38a
     * I want to restart all GUI for displaying stylists.
     *
     * @param list_stylist
     * @param stylist_bitmaps
     */
    public void initializeStylists(ArrayList<Stylist> list_stylist, final HashMap<String, Bitmap> stylist_bitmaps) {


        stylists_list = new ArrayList<Stylist>(sty_hm.values());
        Collections.sort(stylists_list);

        FirebaseWebTasks.ListViewAdpaterStylist la = new FirebaseWebTasks.ListViewAdpaterStylist(MainActivity.this, R.layout.list_view_live_feed, stylists_list);
        ListView lv = (ListView) mCustomFragPageAdapter.getCurrentFragmentView(mViewPager.getCurrentItem()).getView().findViewById(R.id.fragment_livefeed_listview);
        lv.setAdapter(null);
        lv.setAdapter(la);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int position, long l) {
                //Update selected stylist position  index
                stylist_position = position;

                final Stylist s = stylists_list.get(position);


                ((FirebaseWebTasks.ListViewAdpaterStylist) adapterView.getAdapter()).notifyDataSetChanged();
                Toast.makeText(MainActivity.this, s.getName() + " selected.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface MainActivityData {
        MainActivity getMainActivity();

        void setMainActivity(MainActivity ma);
    }
    /////////////////////////////////////////////////////////////////////////////////END CLASS

    /**
     * A placeholder fragment containing store_list simple view.
     */
    public static class TabFragment extends Fragment implements MainActivityData {
        private static final String ARG_SECTION_NUMBER = "section_number";
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private MainActivity ma;
        private ArrayList<UserMessageMetaData> meta_data_list;

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
                    //ma.rootView_LiveTab = rootView;
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.user_messaging_meta_data_layout, container, false);
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

        /**
         * This method will load the meta data of all clients's messages from any stylists. Set a click
         * listener on the list view and start a new activity for a messagig to display actually message history..
         * @param rootView
         */
        private void fragmentView2(View rootView) {
           final ListView lv = (ListView)rootView.findViewById(R.id.user_meta_data_listview);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    UserMessageMetaData meta = (UserMessageMetaData) adapterView.getItemAtPosition(pos);
                    Intent i = new Intent(ma, MessagingActivity.class);
                    i.putExtra("UserMessageMetaData",meta);
                    i.putExtra("user_fb_profile",ma.user_fb_profile);
                    ma.startActivity(i);
                }
            });
            if(meta_data_list == null){//first time initiliazing...
                Log.e("Init","Inititializing tab 3 messeages...");
                final String path = "client_messages_meta_data/"+ma.user_fb_profile.getId();//get client's messages only
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<Map<String,UserMessageMetaData>> gti = new GenericTypeIndicator<Map<String, UserMessageMetaData>>() {};
                        Map<String,UserMessageMetaData> map = null;
                        if(dataSnapshot.getValue() == null){
                            map = new HashMap<String, UserMessageMetaData>();
                        }else{
                            map = dataSnapshot.getValue(gti);
                        }
                        ArrayList<UserMessageMetaData> data_list = new ArrayList<UserMessageMetaData>(map.values());
                        Collections.sort(data_list);
                        meta_data_list = data_list;
                        UserMessageMetaDataAdapter ad =null;
                        if(lv.getAdapter() == null) {
                            ad = new UserMessageMetaDataAdapter(ma, meta_data_list);
                            lv.setAdapter(ad);
                        }else{
                            lv.deferNotifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                            Log.e("Cancelled ","messaging user tab .... errr");
                    }
                });

            }else{

            }



        }

       /* @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {
                int page = this.getArguments().getInt(ARG_SECTION_NUMBER) - 1;
                switch (page){
                    case 0:
                        break;
                    case 1:

                        break;
                }
            }
            else {
            }}*/

        //  private void fragmentView2(View rootView) {
        //      ma.rootView_Reservation = rootView;
        //}

        private void fragmentView0(final View rootView) {
            /// ma.rootView = rootView;
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
                    if (ma.miles == sb.getProgress() + 1) {//repeated
                        return;
                    } else {//get new search results
                        if (ma.store_list != null) ma.store_list.clear();
                        if (ma.stylists_list != null) ma.stylists_list.clear();

                        ma.store_list = null;
                        ma.stylists_list = null;
                        ma.miles = sb.getProgress();
                        // lv = null;
                        // la = null;
                       /* StoresWebTask swt = new StoresWebTask(rootView);
                        swt.execute(sb.getProgress() + "");*/
                        loadStoresFromFirebase();
                    }
                }
            });
            if (ma.store_list == null) {//first time running
               /* StoresWebTask swt = new StoresWebTask(rootView); //old style fetch from my database
                swt.execute(sb.getProgress() + "");*/
                loadStoresFromFirebase();

            } else {///could be here because of screen orient change so update ui

                ListView lv = (ListView) rootView.findViewById(R.id.fragment_listview);
                ListViewAdapter la = new ListViewAdapter(ma, ma.store_list);
                lv.setAdapter(null);
                lv.setAdapter(la);
                ma.showGoogleMaps(rootView, ma.store_list);
            }
            Spinner spinner = (Spinner) rootView.findViewById(R.id.sortBySpinner);
            final String[] sort_arr = {"Distance", "Name"};
            SpinnerDropDownAdapter a = new SpinnerDropDownAdapter(ma, R.id.spinnerDropDownTF, sort_arr);
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(a);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String item = adapterView.getItemAtPosition(i).toString();
                    ListView lv = (ListView) rootView.findViewById(R.id.fragment_listview);
                    if (lv == null || lv.getAdapter() == null) return;//nothing to do
                    ListViewAdapter la = (ListViewAdapter) lv.getAdapter(); //new ListViewAdapter(ma, ma.store_list);

                    switch (item.toLowerCase()) {
                        case "distance":
                            Collections.sort(la.list);
                            break;
                        case "name":
                            Collections.sort(la.list, new Comparator<FirebaseStore>() {
                                @Override
                                public int compare(FirebaseStore s, FirebaseStore ss) {
                                    return s.getName().compareTo(ss.getName());
                                }
                            });
                            break;
                        default:
                            break;
                    }//end swtich
                    la.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        /**
         * HUGE METHOD. THIS method will query the firebase DB and sort the distance based on current user location.
         * Then it populates the store list for program.
         */
        private void loadStoresFromFirebase() {
            ma.store_list = new ArrayList<>();
            final ProgressDialog pd = ProgressDialog.show(ma, "Searching nearby stores", "Please wait...", true, false);
            pd.show();
            if (ma.store_list != null) {//empty list if not null
                ma.store_list.clear();
            }
            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference().child("user");
            db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<List<FirebaseStore>> gti = new GenericTypeIndicator<List<FirebaseStore>>() {
                    };
                    List<FirebaseStore> map = null;
                    if (dataSnapshot.getValue() == null) {
                        map = new ArrayList<FirebaseStore>();
                    } else {

                        map = dataSnapshot.getValue(gti);
                    }
                    ma.store_list = Utils.calculateDistance(ma.user_loc, map, ma.miles);

                    ListView lv = (ListView) ma.mCustomFragPageAdapter.getCurrentFragmentView(ma.mViewPager.getCurrentItem()).getView().findViewById(R.id.fragment_listview);
                    ListViewAdapter la = new ListViewAdapter(ma, ma.store_list);
                    lv.setAdapter(null);
                    lv.setAdapter(la);
                    pd.dismiss();
                    ma.showGoogleMaps(ma.mCustomFragPageAdapter.getCurrentFragmentView(ma.mViewPager.getCurrentItem()).getView(), ma.store_list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    pd.dismiss();
                }
            });


        }

        /**
         * This is the View to be inflated for the 2 tab or (tab #=1). Stylist View
         * This method seems to only get called when begin of app. TTHUS it really doesnt function unless
         * on tab listener........
         * This only gets created once because of the TAB LISTENER ANDROID functionality
         */
        private void fragmentView1(View rootView) {
            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Snackbar.make(view, "Make Reservation", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    ma.showCreditCard();

                }
            });
        }

        @Override
        public MainActivity getMainActivity() {
            return this.ma;
        }

        @Override
        public void setMainActivity(MainActivity ma) {
            this.ma = ma;
        }
    }

    /**
     * This is for Store list.
     * ArrayAdpater for stores.
     */
    public static class ListViewAdapter extends ArrayAdapter<FirebaseStore> {
        private MainActivity ma;
        private ArrayList<FirebaseStore> list;

        public ListViewAdapter(MainActivity ma, ArrayList<FirebaseStore> values) {
            super(ma, R.layout.list_view_layout, values);
            this.list = values;

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
            double miles_away = store.getMiles_away();
            DecimalFormat df = new DecimalFormat("0.##");

            // r.setText("Shop: " + store.getName() + "\n" + "Miles away: " + df.format(miles_away) + "\nHours: " + store.formatHoursTo12hours() + "\n" + store.displayIsAvailable());//+"\n" + "Address: " + store.getAddress().toUpperCase() + "\n" + store.getCitystate().toUpperCase());
            TextView t = (TextView) rowView.findViewById(R.id.shopNameTextView);
            t.setText(store.getName());
            TextView tt = (TextView) rowView.findViewById(R.id.distanceTextView);
            tt.setText(df.format(miles_away)+" mi");
            TextView ttt = (TextView) rowView.findViewById(R.id.hoursTextView);
            ttt.setText(store.formatHoursTo12hours());

            r.setChecked(position_item == ma.selectedPosition);
            r.setTag(position_item);
            r.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position_item != ma.selectedPosition) {//new store...go get the stylists from that db
                        ma.stylists_list = null;//new store null stylists
                    }
                    int LAST_PICKED = ma.selectedPosition;
                    ma.selectedPosition = (Integer) view.getTag();
                    if (ma.selectedPosition != LAST_PICKED) {
                        // Log.e("DIFF CHOICE::","sp: "+ma.selectedPosition+" <> lastpicked: "+LAST_PICKED);
                        ma.STYLIST_BITMAPS_LOADED = false;
                        if (ma.stylists_list != null) ma.stylists_list.clear();
                        if (stylist_bitmaps != null) stylist_bitmaps.clear();
                        stylist_bitmaps = null;
                        ma.stylists_list = null;
                    }
                    notifyDataSetChanged();//updates the button click isset
                    LatLng myLoc = store.getLocation();
                    // Updates the location and zoom of the MapView
                    com.google.android.gms.maps.model.LatLng l = new com.google.android.gms.maps.model.LatLng(myLoc.latitude, myLoc.longitude);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(l, 14);//20 is closeset 5 is largest
                    ma.gm.animateCamera(cameraUpdate);
                    ma.mv.onResume();
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
        private MainActivity ma;
        private HashMap<Integer, Fragment> map;

        public CustomFragPageAdapter(FragmentManager fm, MainActivity ma) {
            super(fm);
            this.ma = ma;
            this.map = new HashMap<>();
        }

        public Fragment getCurrentFragmentView(int index) {
            return this.map.get(index);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return store_list TabFragment (defined as store_list static inner class above).
            switch (position) {
                case 0:
                    TabFragment tf = TabFragment.newInstance(position + 1);
                    tf.setMainActivity(ma);
                    map.put(position, tf);
                    return tf;
                case 1:
                    TabFragment tf2 = TabFragment.newInstance(position + 1);
                    tf2.setMainActivity(ma);
                    map.put(position, tf2);
                    return tf2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(MainActivity.this.user_fb_profile != null && MainActivity.this.user_fb_profile.getId()!=null)return 3;
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Store Location";
                case 1:
                    return "Live Feed";
                // case 2:
                //   return "Make Reservation";
            }
            return null;
        }
    }

    /**
     * REQUEST A AD
     */
    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = null;

        if (ADTESTING) {
            adRequest = new PublisherAdRequest.Builder()
                    .addTestDevice("23B075DED4F5E3DB63757F55444BFF46")
                    .build();
        } else {

            try {
                adRequest = new PublisherAdRequest.Builder().setBirthday(sdf.parse(AD_AGE_DATE_STRING))
                        .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        mPublisherInterstitialAd.loadAd(adRequest);
    }

    static class SpinnerDropDownAdapter extends ArrayAdapter<String> {

        public SpinnerDropDownAdapter(Context context, int tv, String[] objects) {
            super(context, tv, objects);
        }

        @Override
        public View getView(int pos, View view, ViewGroup vg) {
            View v = View.inflate(this.getContext(), R.layout.spinner_sort_by_layout, null);
            TextView tv = (TextView) v.findViewById(R.id.spinnerDropDownTF);
            tv.setText(this.getItem(pos));
            return v;
        }

        @Override
        public View getDropDownView(int pos, View view, ViewGroup bg) {
            View v = View.inflate(this.getContext(), R.layout.spinner_sort_by_layout, null);
            TextView tv = (TextView) v.findViewById(R.id.spinnerDropDownTF);
            tv.setText(this.getItem(pos));
            return v;
        }
    }
}

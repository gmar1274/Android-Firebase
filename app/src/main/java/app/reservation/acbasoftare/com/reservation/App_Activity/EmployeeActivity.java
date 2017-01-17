package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.Manifest;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Objects.Reservation;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.ExpandableListView.ExpandableListViewAdapter;
import app.reservation.acbasoftare.com.reservation.ListAdapters.DateArrayAdapter;
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
    public static String stylist_id;
    public static String store_id;
    public static EmployeeActivity employeeActivity;
    private SectionsPagerAdapter mCustomFragPageAdapter;
    private TabLayout tabLayout;
    public static Stylist stylist;
    public static View tab3, tab2;
    public static ExpandableListViewAdapter lva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        employeeActivity=this;
        store_id=getIntent().getStringExtra("store_id");
        stylist_id=getIntent().getStringExtra("stylist_id");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager=(ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
///////////////////////////////////////////////////////////////
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_employeeactivity);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return store_list fragment for each of the three
        // primary sections of the activity.
        mCustomFragPageAdapter=new EmployeeActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager=(ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mCustomFragPageAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 1) {
                    if(reservation == null || lva == null || tab2 == null) return;//break if null
                    fragmentTab2();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout=(TabLayout) findViewById(R.id.tabs_employeeactivity);
        tabLayout.setupWithViewPager(mViewPager);

        /////////////////////////

    }

    private void fragmentTab2() {
        ListView lv=(ListView) tab2.findViewById(R.id.listview_employee_activity);
        ArrayList<String> days=reservation.getDaysReserved();//for given stylist
        DateArrayAdapter la=new DateArrayAdapter(this, R.layout.expandable_list_item, days);
        lv.setAdapter(la);
    }

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

    public static String getStoreID() {return store_id;}

    public static String getStylistID() {return stylist_id;}

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER="section_number";

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
                    rootView=inflater.inflate(R.layout.fragment_employee_today_layout, container, false);///this is the fragment view
                    break;
                case 1:
                    rootView=inflater.inflate(R.layout.fragment_employee_upcoming_layout, container, false);
                    tab2=rootView;
                    break;
                case 2:
                    rootView=inflater.inflate(R.layout.fragment_employee_activity_settings_layout, container, false);
                    tab3=rootView;
                    break;
            }
            return displayView(rootView, page - 1);
        }

        /*This method is responsible for displaying the appropriate Views
              * I guess the layouts are formed before even switching to a tab, so on startup up...
              * */
        private View displayView(View rootView, int page) {

            switch(page) {
                case 0:
                    fragmentView0(rootView);
                    break;
                case 1:
                    break;
                case 2:
                    fragmentView2(rootView);
                    break;
            }
            return rootView;
        }

        private void fragmentView2(View rootView) {
            ImageView iv=(ImageView) rootView.findViewById(R.id.imageView_employee_activity);
            iv.setImageBitmap(stylist.getImage());
            TextView tv=(TextView) rootView.findViewById(R.id.textView_tab3_employee_activity);
            tv.setText(tv.getText() + stylist.getName().toUpperCase());
            Button b=(Button) rootView.findViewById(R.id.button_upload_profile_pic);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openImageGallery();
                }
            });
        }

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
                StylistWebTaskAppointments swt=new StylistWebTaskAppointments(rootView, lv);
                swt.execute();
            }
        }
    }

    /**
     * A {@link SectionsPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fragmentManager) {super(fragmentManager);}

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Today's Appointments";
                case 1:
                    return "Upcoming Appointments";
                case 2:
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

    public static void openImageGallery() {
        galleryIntent();
        if(Build.VERSION.SDK_INT >= 23) {
            // Here, thisActivity is the current activity
            if(ContextCompat.checkSelfPermission(employeeActivity.getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                       != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if(ActivityCompat.shouldShowRequestPermissionRationale(employeeActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showReason();
                } else {
                    ActivityCompat.requestPermissions(employeeActivity,
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

    private static void galleryIntent() {
        Intent intent=new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        employeeActivity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }

    private static void showReason() {
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(employeeActivity);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage("Needed to upload profile picture.");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(employeeActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
                ImageView imageView = (ImageView) tab3.findViewById(R.id.imageView_employee_activity);
                imageView.setImageBitmap(bitmap);
                UploadImageWebTask u = new UploadImageWebTask(bitmap);
                u.execute();
                stylist.setBitmap(bitmap);
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

}

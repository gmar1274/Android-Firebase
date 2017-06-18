package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.CustomFBProfile;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessagingUserMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.Interfaces.IBio;
import app.reservation.acbasoftare.com.reservation.Interfaces.IPortfolio;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Recycleview.RecycleViewBioProfileAdapter;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * This class needs 4 objects.
 * 1) CustomFBProfile - user info
 * 2) Firebase store - What store the stylist is associated with
 * 3) Stylist - to contact/display info
 * 4) HashMap<String,String> - file locations of bitmaps per id
 */
public class StylistBioScrollingActivity extends AppCompatActivity implements IBio,IPortfolio {

    private boolean isStore;
    private FirebaseStore store;
    private Stylist stylist;
    private CustomFBProfile profile;
    private HashMap<String,String> locations;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("locations", locations);
        outState.putParcelable("profile", profile);
        outState.putParcelable("store", store);
        outState.putParcelable("stylist", stylist);
        outState.putBoolean("isstore",isStore);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stylist_bio_scrolling);


        if(savedInstanceState != null){
            this.store = savedInstanceState.getParcelable("store");
            this.stylist = savedInstanceState.getParcelable("stylist");
            this.profile = savedInstanceState.getParcelable("profile");
            this.locations = (HashMap<String, String>) savedInstanceState.getSerializable("locations");
            this.isStore = savedInstanceState.getBoolean("isstore");
        }else {
            Intent intent = getIntent();
            if (intent.getParcelableExtra(Utils.STORE) != null) {
                store = intent.getParcelableExtra(Utils.STORE);
            }
            if(intent.getParcelableExtra(Utils.STYLIST) != null){
                stylist = intent.getParcelableExtra(Utils.STYLIST);
                if(stylist.getId().equalsIgnoreCase(Utils.STORE_ID)){
                    isStore=true;
                }
            }
            if(intent.getParcelableExtra(Utils.PROFILE) != null){
                profile = intent.getParcelableExtra(Utils.PROFILE);
            }
            if(intent.getSerializableExtra(Utils.LOCATIONS) != null){
                locations = (HashMap<String, String>) intent.getSerializableExtra(Utils.LOCATIONS);
            }

        }
        ActionBar ab =  getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(stylist.getName().toUpperCase());


        final Button call = (Button)findViewById(R.id.button_call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(stylist.getPhone());
            }
        });


        Button msg = (Button)findViewById(R.id.button_message);
        msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMessagingActivity(profile,store,stylist,locations);
            }
        });

        RatingBar rbar = (RatingBar)findViewById(R.id.ratingBar);

        TextView status = (TextView)findViewById(R.id.textView_active);

        TextView name = (TextView)findViewById(R.id.textView_sty_name);
        name.setText(stylist.getName().toUpperCase());
        status.setText((stylist.isAvailable())==true ? "Active":"Not Active");

        RecyclerView sty_images = (RecyclerView)this.findViewById(R.id.stylist_images_rv);
        fetchStylistImages(stylist,sty_images);
        RecyclerView portfolio = (RecyclerView)this.findViewById(R.id.portfolio_images_rv);
        LinearLayoutManager horizontal2 = new LinearLayoutManager(this);
        fetchPortfolioImages(stylist,portfolio);
        if(isStore){
            msg.setVisibility(View.GONE);
            ab.setTitle(store.getName().toUpperCase());
            name.setText(store.getName().toUpperCase());
        }

    }
    @Override
    public void onBackPressed(){
        super.finish();
    }

    /**
     * Call intent. (Or dial intent)
     * @param numberTocall
     */
    @Override
    public void call(String numberTocall) {
        try {
            Log.e("B4:",numberTocall+" <> AFTER: "+numberTocall.trim());
            String uri = "tel:" + numberTocall.trim();
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse(uri));
            this.startActivity(i);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Create data to send to messengingActivity.java
     * @param prof user profile
     * @param store store attributes
     * @param s stylist attributes
     * @param stylist_bitmaps file locations of all bitmaps per id as key
     */
    @Override
    public void startMessagingActivity(CustomFBProfile prof, FirebaseStore store, Stylist s, HashMap<String,String> stylist_bitmaps) {
        Intent i = new Intent(this, MessagingActivity.class);
        FirebaseMessagingUserMetaData userMeta = new FirebaseMessagingUserMetaData(prof);
        FirebaseMessagingUserMetaData selectedUser = new FirebaseMessagingUserMetaData(store, s);
        i.putExtra(Utils.USER, userMeta);
        i.putExtra(Utils.SELECTED_USER, selectedUser);
        i.putExtra(Utils.USER_BITMAP_LOCATION,stylist_bitmaps.get(prof.getId()));
        i.putExtra(Utils.SELECTED_USER_BITMAP_LOCATION,stylist_bitmaps.get(s.getId()));
        this.startActivity(i);
    }

    @Override
    public void fetchPortfolioImages(Stylist stylist, RecyclerView rv) {

    }

    @Override
    public void fetchStylistImages(Stylist stylist, RecyclerView rv) {
        LinearLayoutManager horizontal = new LinearLayoutManager(this);
        horizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(horizontal);
        ArrayList<String> list= new ArrayList<>();
        list.add(locations.get(this.stylist.getId()));
        rv.setAdapter(new RecycleViewBioProfileAdapter(this,list,R.layout.imageview));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

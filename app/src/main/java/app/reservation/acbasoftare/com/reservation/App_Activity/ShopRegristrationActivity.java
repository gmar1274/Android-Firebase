package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.R;

public class ShopRegristrationActivity extends FragmentActivity {


    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_regristration);

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

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {


                // TODO: Get info about the selected place.
                Log.i("PLACE: ", "Place: " + place.getName() + "\n" + place.getAttributions()+"\n"+place.getWebsiteUri());
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
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:");
        SimpleDateFormat ampm = new SimpleDateFormat("hh:mm a");
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

        MainActivity.SpinnerDropDownAdapter a = new MainActivity.SpinnerDropDownAdapter(this,R.id.spinnerDropDownTF,values);
        Spinner smo = (Spinner)findViewById(R.id.spinner_mon_open);
        Spinner smc = (Spinner)findViewById(R.id.spinner_mon_close);

        Spinner sto = (Spinner)findViewById(R.id.spinner_tues_open);
        Spinner stc = (Spinner)findViewById(R.id.spinner_tues_close);

        Spinner swo = (Spinner)findViewById(R.id.spinner_wed_open);
        Spinner swc = (Spinner)findViewById(R.id.spinner_wed_close);

        Spinner stho = (Spinner)findViewById(R.id.spinner_thurs_open);
        Spinner sthc = (Spinner)findViewById(R.id.spinner_thurs_close);

        Spinner sfo = (Spinner)findViewById(R.id.spinner_fri_open);
        Spinner sfc = (Spinner)findViewById(R.id.spinner_fri_close);

        Spinner sso = (Spinner)findViewById(R.id.spinner_sat_open);
        Spinner ssc = (Spinner)findViewById(R.id.spinner_sat_close);

        Spinner suno = (Spinner)findViewById(R.id.spinner_sun_open);
        Spinner sunc = (Spinner)findViewById(R.id.spinner_sun_close);



    }
}

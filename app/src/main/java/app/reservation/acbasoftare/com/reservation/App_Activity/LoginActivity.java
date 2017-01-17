package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Services.GPSLocation;
import app.reservation.acbasoftare.com.reservation.WebTasks.Login;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";
    /**
     * Id to identity READ_CONTACTS permission request.
     * cntrl alt L for formatting
     * cntrl  / for commenting
     */
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private SharedPreferences pref;
    private PublisherInterstitialAd mPublisherInterstitialAd;
    public static GPSLocation gps;
    private CallbackManager callbackManager;

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice("23B075DED4F5E3DB63757F55444BFF46")
                .build();
        mPublisherInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
       AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
         callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.fb);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              MainActivity.user_fb_profile= Profile.getCurrentProfile();
               goToMainActivity();
            }

            @Override
            public void onCancel() {
                Log.d("onCancel","");
                // App code
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("onError",error.toString());
                error.printStackTrace();
            }

        });


        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        gps = new GPSLocation(LoginActivity.this);
                        if(!gps.canGetLocation()){
                            showSettingsAlert();
                        }
                    }

                }
        );

        // Toast.makeText(this,"Lat: "+gps.getLatitude()+"Lon: "+gps.getLongitude(),Toast.LENGTH_LONG).show();

        mPublisherInterstitialAd = new PublisherInterstitialAd(this);
        mPublisherInterstitialAd.setAdUnitId(getString(R.string.login_video_ad_unit_id));
        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                goToMainActivity();
            }
        });

        requestNewInterstitial();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9309556355508377~8508953646");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("23B075DED4F5E3DB63757F55444BFF46").build();
        mAdView.loadAd(adRequest);
        //////////load Ad

       //////////
        pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
       // pref.edit().putString(PREF_USERNAME,null).putString(PREF_PASSWORD,null).commit();//debug clear memory of use
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        if (username != null && password != null) {
            Login login = new Login(this);
            login.execute(username, Encryption.encryptPassword(password));
            this.setVisible(true);
            return;
        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mGuestButton = (Button) findViewById(R.id.guest_btn);
        mGuestButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainActivity();
            }
        });
        Button shop_owner = (Button) this.findViewById(R.id.storeLoginBtn);
        shop_owner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShopLogin();
            }
        });

    }

    private void goToShopLogin() {
        this.startActivity(new Intent(this,ShopLoginActivity.class));
        this.finish();

    }

    private void debug() {
        this.startActivity(new Intent(this,TicketScreenActivity.class));

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } /*else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }*/
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Password is required");
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            ///attempt to login here at this point
            // showProgress(true);
            // mAuthTask = new UserLoginTask(email, password);

            Login login = new Login(this, mEmailView, mPasswordView, pref);
            login.execute(email, Encryption.encryptPassword(password));//sends sha1 encrypted password

            //showProgress(false);

        }
    }

    private void goToMainActivity() {
        gps.getLocation();
        if (mPublisherInterstitialAd.isLoaded()) {
            mPublisherInterstitialAd.show();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
        this.finish();
        return;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >0;
    }
    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
public static void debugDisplayGPS(Activity a){
    Toast.makeText(a,"Updated GPS: "+gps.getLocation(),Toast.LENGTH_LONG).show();
}
}


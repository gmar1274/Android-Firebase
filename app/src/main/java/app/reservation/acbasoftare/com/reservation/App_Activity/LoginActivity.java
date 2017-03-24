package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.ProfileFB;
import app.reservation.acbasoftare.com.reservation.App_Services.GPSLocation;
import app.reservation.acbasoftare.com.reservation.FirebaseWebTasks.FirebaseEmployeeLogin;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.Login;

import static com.google.api.client.http.HttpMethods.HEAD;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {
    public static final boolean ADTESTING = false;//false means live
    public  final String PREF_USERNAME = "username";
    public  final String PREF_PASSWORD = "password";

    public static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    public static final String AD_AGE_DATE_STRING = "01/01/2005";

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
    public  GPSLocation gps;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pd;

    private void requestNewInterstitial() {
        PublisherAdRequest adRequest = null;
        if(ADTESTING){
            adRequest = new PublisherAdRequest.Builder()
                    .addTestDevice("23B075DED4F5E3DB63757F55444BFF46")
                    .build();
        }
        else{

            try {
                adRequest = new PublisherAdRequest.Builder().setBirthday(sdf.parse(AD_AGE_DATE_STRING))

                        .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        mPublisherInterstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = (LoginButton) findViewById(R.id.fb);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              //MainActivity.user_fb_profile= Profile.getCurrentProfile();
                mAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Profile profile = Profile.getCurrentProfile();
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        ProfileFB prof = new ProfileFB(profile);
                        i.putExtra("fb_profile", prof);
                        i.putExtra("gps",gps);
                        Log.d("fb","login");
                        LoginActivity.this.startActivity(i);
                        LoginActivity.this.finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this,"Network error. :(",Toast.LENGTH_LONG).show();
                    }
                });


               //goToMainActivity();
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
                 Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.putExtra("gps",gps);
                LoginActivity.this.startActivity(i);
                LoginActivity.this.finish();
            }
        });


        requestNewInterstitial();
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-9309556355508377~8508953646");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        //mAdView.setAdUnitId("ca-app-pub-9309556355508377/8229752040");
       // mAdView.setAdSize(AdSize.SMART_BANNER);
        AdRequest adRequest = null;
        if(ADTESTING){
            adRequest  = new AdRequest.Builder().addTestDevice("23B075DED4F5E3DB63757F55444BFF46").build();
        }
        else{
            try {
                adRequest = new AdRequest.Builder().setBirthday(sdf.parse(AD_AGE_DATE_STRING))

                                    .build();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mAdView.loadAd(adRequest);
        //////////load Ad
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
       //////////
        pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
       // pref.edit().putString(PREF_USERNAME,null).putString(PREF_PASSWORD,null).commit();//debug clear memory of use
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        if (username != null && password != null) {
            signIn(username,password);
            //Login login = new Login(this);
            //login.execute(username, Encryption.encryptPassword(password));
            return;
        }

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
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
                FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            goToMainActivity();
                        } else {
                            Log.e("ON COMPLETE AUTH", "AUTH ERROR");
                          if(pd!=null)  pd.dismiss();
                        }
                    }
                }).addOnFailureListener(LoginActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        if(pd!=null)pd.dismiss();
                    }
                });
            }


        });
        Button shop_owner = (Button) this.findViewById(R.id.storeLoginBtn);
        shop_owner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShopLogin();
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
               if(pd==null)return;
                if(mEmailView==null || mPasswordView==null || mEmailView.getText().length() == 0 || mPasswordView.getText().length() == 0 ){return;}
                Log.e("FIREBASE LISTENER:","HERE IN LISTENER");
                pd.dismiss();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user != null){
                    goToMainActivity();
                }else{
                    errorWithSignIn();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void test() {
        Intent i = new Intent(this, ShopRegristrationActivity.class);
        this.startActivity(i);
        this.finish();
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
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

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
////////sign in listener for firebase
            ///sign in if user in firebase otherwise sign in anon to check if stylist
            signIn(email,password);

        }
    }

    private void signIn(final String email, final String password){
        pd = ProgressDialog.show(this,"Authenticating","Please wait...",true,false);
        pd.show();
       FirebaseAuth.getInstance().signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseEmployeeLogin fe = new FirebaseEmployeeLogin(LoginActivity.this, email, password, mEmailView, mPasswordView, pd);
                            fe.execute();

                            //LOGIN USING MY SERVER
                            // myLogin(email,password);
                        }
                    });
    }
    private void myLogin(String email, String password) {
        Login login = new Login(this, mEmailView, mPasswordView, pref);
        login.execute(email, Encryption.encryptPassword(password));//sends sha1 encrypted password
    }

    @Override
public void onStart(){
    super.onStart();
   if(mAuth!=null && mAuthListener!=null) mAuth.addAuthStateListener(this.mAuthListener);
}
@Override
public void onStop(){
    super.onStop();
   if(this.mAuthListener != null){
        mAuth.removeAuthStateListener(this.mAuthListener);
    }
}

    private void goToMainActivity() {
        if(!gps.canGetLocation())gps.getLocation();

        if (mPublisherInterstitialAd.isLoaded()) {
            mPublisherInterstitialAd.show();
        } /*else {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
        }*/
    }
    private void errorWithSignIn() {
        this.mEmailView.setError("Email/Username may be wrong");
        this.mPasswordView.setError("Password may be wrong");
    }
    @Override
    public void onBackPressed() {
        this.finish();
        return;
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return (email.contains("@")&&email.contains(".")) || email.length()>3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 5;
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
public  void debugDisplayGPS(Activity a){
    Toast.makeText(a,"Updated GPS: "+gps.getLocation(),Toast.LENGTH_LONG).show();
}
}


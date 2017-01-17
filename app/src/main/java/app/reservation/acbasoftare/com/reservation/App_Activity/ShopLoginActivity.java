package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.ParamPair;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;

/**
 * A login screen that offers login via email/password.
 */
public class ShopLoginActivity extends Activity  {

    private UserLoginTask mAuthTask=null;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
         private View mLoginFormView;
    private FirebaseAuth mAuth;
   /// private  FirebaseAuth.AuthStateListener mAuthListener;
    private final String TAG = "FIREBAS AUTH:: ";
    @Override
    public void onStart() {
        super.onStart();
       // mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
       // if(mAuthListener != null) {
        //    mAuth.removeAuthStateListener(mAuthListener);
        //}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);

        mAuth = FirebaseAuth.getInstance();

       /** mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    goToTicketActivity();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        */
        // Set up the login form.
        mEmailView=(AutoCompleteTextView) findViewById(R.id.email);


        mPasswordView=(EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if(id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton=(Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView=findViewById(R.id.email_login_form);
        mProgressView=findViewById(R.id.login_progress);

        Button reg = (Button) this.findViewById(R.id.registerBtn);
        reg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                    registerToday();
            }
        });
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if(mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email=mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();


        boolean cancel=false;
        View focusView=null;

        // Check for a valid password, if the user entered one.
        if(!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView=mPasswordView;
            cancel=true;
        }

        // Check for a valid email address.
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView=mEmailView;
            cancel=true;
        } else if(!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView=mEmailView;
            cancel=true;
        }

        if(cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            password = Encryption.encryptPassword(password);
            final ProgressDialog pd=ProgressDialog.show(this, "Authenticating", "Please wait...", true, false);
            pd.show();

            //showProgress(true);
           // mAuthTask=new UserLoginTask(email, password, mAuth);
            //mAuthTask.execute((Void) null);
            final String finalPassword=password;
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pd.dismiss();
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                       // Log.w(TAG, "signInWithEmail:failed", task.getException());
                        Toast.makeText(ShopLoginActivity.this,"Failed to login.",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ShopLoginActivity.this,"LOGIN COMPLETED.",
                                Toast.LENGTH_LONG).show();
                         goToTicketActivity(email, finalPassword);
                    }

                    // ...
                }
            });
        }
    }



    private void registerToday() {
            this.startActivity(new Intent(this,RegisterShopActivity.class));
            //this.finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length()>=5;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime=getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail=email;
            mPassword=password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                ArrayList<ParamPair> l = new ArrayList<>();
                l.add(new ParamPair("store_login", Encryption.encryptPassword("acbastore_loginacba")));
                l.add(new ParamPair("username",mEmail));
                l.add(new ParamPair("password",Encryption.encryptPassword(mPassword)));
                JSONObject obj = new WebService().makeHttpRequest(WebService.storeLoginURL,l);
                JSONArray arr = obj.getJSONArray("status");
                String succ = arr.getJSONObject(0).getString("success");

                if(succ.equalsIgnoreCase("true")){
                        return true;
                }else{
                    return false;
                }
            } catch(Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask=null;
            showProgress(false);

            if(success) {
               ShopLoginActivity.this.goToTicketActivity(mEmail,mPassword);
            } else {
                mEmailView.setError("Username may be incorrect");
                mEmailView.requestFocus();
                mPasswordView.setError("Password may be incorrect");
            }
        }



        @Override
        protected void onCancelled() {
            mAuthTask=null;
            showProgress(false);
        }
    }
    private void goToTicketActivity(String email,String password){
        Intent i = new Intent(ShopLoginActivity.this, TicketScreenActivity.class);
        i.putExtra("email",email);
        i.putExtra("password",password);
        ShopLoginActivity.this.startActivity(i);
        ShopLoginActivity.this.finish();
    }
}

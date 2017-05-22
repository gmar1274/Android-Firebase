package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseEmployee;

/**
 * Created by user on 2017-02-11.
 * Firebase connection. Query firebase searching for the username and password match.
 */
public class FirebaseEmployeeLogin extends AsyncTask<String, Void, String> {
    private String user,pass,pass_orig;
    private ProgressDialog pd;
    private Context c;
    private AutoCompleteTextView emailView;
    private EditText passView;

    public FirebaseEmployeeLogin(Context c , String user, String pass, AutoCompleteTextView emailView, EditText passView){
        this.user = user;
        this.pass = Encryption.encryptPassword(pass);
        this.c = c;
        this.emailView = emailView;
        this.passView = passView;
        this.pass_orig=pass;
    }
    public FirebaseEmployeeLogin(Context c , String user, String pass, AutoCompleteTextView emailView, EditText passView,ProgressDialog pd){
        this.user = user;
        this.pass = Encryption.encryptPassword(pass);
        this.c = c;
        this.emailView = emailView;
        this.passView = passView;
        this.pd = pd;
        this.pass_orig=pass;
    }

    @Override
    public void onPreExecute(){
       if(pd==null){
           this.pd = ProgressDialog.show(c,"Authenticating","Please wait...",true,false);
       }
    }
    @Override
    public void onPostExecute(String result){
      //searching async
    }
    @Override
    protected String doInBackground(String... voids) {
        return searchEmployee();
    }

    /**
     * THIS METHOD WILL QUEURY firebase employees URL get a MAP of all FIREBASEemployees and check if the username and password exists.
     * If yes then will start the Activity...
     * @return
     */
    private String searchEmployee(){
        String hashedID = String.valueOf(user.toString().hashCode());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employees/"+hashedID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null){
                    pd.dismiss();
                    showError();
                    return;
                }
                //FirebaseEmployee temp = new FirebaseEmployee(user,pass);
                //GenericTypeIndicator<Map<String,FirebaseEmployee>> gti= new GenericTypeIndicator<Map<String,FirebaseEmployee>>(){};
               // Map<String,FirebaseEmployee> map = dataSnapshot.getValue(gti);
                FirebaseEmployee stylist = dataSnapshot.getValue(FirebaseEmployee.class);
                pd.dismiss();
               // FirebaseEmployee stylist = map.get(String.valueOf(user.toString().hashCode()));
                if(stylist != null && user.equals(stylist.getApp_username()) &&  stylist.getApp_password().equals(pass)){
                    loginSucces(stylist);
                }else{
                    showError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pd.dismiss();
                Log.e("FIREBASE ERROR","firebase login error");
            }
        });
        return "";
    }
    private void loginSucces(FirebaseEmployee emp){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
        LoginActivity la = (LoginActivity) c;
        pref.edit().putString(la.PREF_USERNAME,emp.getApp_username()).putString(la.PREF_PASSWORD, pass_orig).commit();
        Intent i = new Intent(c, EmployeeActivity.class);
        i.putExtra("employee",emp);
        i.putExtra("PREF_USERNAME",la.PREF_USERNAME);
        i.putExtra("PREF_PASSWORD",la.PREF_PASSWORD);
        c.startActivity(i);
    }
    private void showError() {
        emailView.setError("Username may be incorrect");
        passView.setError("Password may be incorrect");
    }
}

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
import android.widget.NumberPicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseEmployee;

/**
 * Created by user on 2017-02-11.
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

    }
    @Override
    protected String doInBackground(String... voids) {

        return searchEmployee();
    }
    private String searchEmployee(){

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employees");
        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseEmployee temp = new FirebaseEmployee(user,pass);
                GenericTypeIndicator<List<FirebaseEmployee>> gti= new GenericTypeIndicator<List<FirebaseEmployee>>(){};
                List<FirebaseEmployee> list = dataSnapshot.getValue(gti);
                pd.dismiss();
                if(list.contains(temp)){//found
                    loginSucces(list.get(list.indexOf(temp)));
                }else{//not found
                    //Log.e("user: ",user+" pass:"+pass);
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

        pref.edit().putString(LoginActivity.PREF_USERNAME,emp.getApp_username()).putString(LoginActivity.PREF_PASSWORD, pass_orig).commit();
        Intent i = new Intent(c, EmployeeActivity.class);
        i.putExtra("employee",emp);
        c.startActivity(i);
    }
    private void showError() {
        emailView.setError("Username may be incorrect");
        passView.setError("Password may be incorrect");
    }
}

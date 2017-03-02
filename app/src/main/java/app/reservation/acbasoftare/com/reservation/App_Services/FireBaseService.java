package app.reservation.acbasoftare.com.reservation.App_Services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

import app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity;

/**
 * Created by user on 2016-11-22.
 */
public class FireBaseService implements FirebaseAuth.AuthStateListener{

    private FirebaseAuth mAuth;
    private Activity activity;
  //  private User user;
    public  FireBaseService(Activity a){
        mAuth = FirebaseAuth.getInstance();
        this.activity=a;
       // this.user=new User();
    }
    public void createNewUser(String email,String password){

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this.activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d("Create", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                     //   if (!task.isSuccessful()) {
                          // Toast.makeText(LoginActivity.class,"Success",  Toast.LENGTH_SHORT).show();
                     //   }

                    }
                });
// [END create_user_with_email]
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

    }
    public  void  addListener(){
        this.mAuth.addAuthStateListener(this);
    }
    public void removeListener(){
        this.mAuth.removeAuthStateListener(this);
    }
    public void sendMessage(String message){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue(message);
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d("", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException());
            }
        });
    }
  /*  private class User extends FirebaseMessagingService{
      public User(){

      }
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // TODO(developer): Handle FCM messages here.
            // If the application is in the foreground handle both data and notification messages here.
            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
            Log.d("", "From: " + remoteMessage.getFrom());
            Log.d("", "Notification Message Body: " + remoteMessage.getNotification().getBody());
        }
    }
    */
}

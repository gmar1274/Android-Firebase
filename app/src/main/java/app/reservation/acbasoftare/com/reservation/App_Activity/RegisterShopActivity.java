package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.Dialog.CreditCardDialog;
import app.reservation.acbasoftare.com.reservation.R;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class RegisterShopActivity extends Activity {
    private final String registerURL= "http://www.acbasoftware.com/";
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);
        mAuth =FirebaseAuth.getInstance();
       //WebView wv = (WebView) this.findViewById(R.id.webView);
       // wv.loadUrl(registerURL);
        Button reg = (Button)this.findViewById(R.id.newMemberBtn);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //debug();
               // showCreditCard();
            }
        });


    }

    /**
     * Create TEST USER to firebase
     */
    private void debug() {
        final ProgressDialog pd = ProgressDialog.show(this,"Registrating","Please Wait...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        EditText user = (EditText) this.findViewById(R.id.emailEditText);
        EditText pass = (EditText)this.findViewById(R.id.passwordEditText);
        final String email = user.getText().toString();
        final String password =Encryption.encryptPassword(pass.getText().toString());
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                pd.dismiss();//done registering
                if(task.isSuccessful()){
                    Toast.makeText(RegisterShopActivity.this,"Successfully Registered!",Toast.LENGTH_LONG).show();
                    goToTicketScreen(email,password);
                }else{
                    Toast.makeText(RegisterShopActivity.this,"Error in Registration...",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void goToTicketScreen(String email, String password){
        Intent i = new Intent(this,TicketScreenActivity.class);
        i.putExtra("email",email);
        i.putExtra("password",password);
        this.startActivity(i);
        this.finish();
    }
    private void showCreditCard(){
        CreditCardDialog ccd = new CreditCardDialog(this);
        ccd.newMemberShipShowCreditCardDialog(this,null);
    }

    @Override
    public void onBackPressed(){
        //figure what to do
    }
}

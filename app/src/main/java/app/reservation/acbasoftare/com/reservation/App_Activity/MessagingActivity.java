package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import com.facebook.Profile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;
import app.reservation.acbasoftare.com.reservation.ListAdapters.UserViewMessagesInboxAdapter;
import app.reservation.acbasoftare.com.reservation.R;

public class MessagingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////
        UserMessageMetaData meta = getIntent().getParcelableExtra("UserMessageMetaData");
        Profile profile = getIntent().getParcelableExtra("user_fb_profile");
        //firebase url: client_messages/client_id/sty_id {list of messages}

        final ListView lv = (ListView)this.findViewById(R.id.message_listview);


        final String path = "client_messages/"+profile.getId()+"/"+meta.getStylist_id();//
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseMessage> message_list = null;
                GenericTypeIndicator<List<FirebaseMessage>> gti = new GenericTypeIndicator<List<FirebaseMessage>>() {};
                if(dataSnapshot.getValue() == null){
                    message_list = new ArrayList<FirebaseMessage>();
                }else{
                    message_list = dataSnapshot.getValue(gti);
                }
                Collections.sort(message_list);//sort by timestamp
                if(lv.getAdapter() == null){
                    UserViewMessagesInboxAdapter ad = new UserViewMessagesInboxAdapter(MessagingActivity.this, message_list);
                }else{

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("message err","message errors on inbox ...");
            }
        });

    }

}

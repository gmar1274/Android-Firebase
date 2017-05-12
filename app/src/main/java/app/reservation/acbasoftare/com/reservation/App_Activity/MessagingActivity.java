package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseInboxMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessagingUserMetaData;
import app.reservation.acbasoftare.com.reservation.Interfaces.IMessaging;
import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;
import app.reservation.acbasoftare.com.reservation.ListAdapters.MessagingListAdapter;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

public class MessagingActivity extends AppCompatActivity implements IMessaging {
    private Bitmap user_bitmap;
    private ListView messagesListView;///////this is the messaging list view
    private Bitmap selectedUserBitmap;
    private FirebaseMessagingUserMetaData selectedUserMeta, userMeta;
    private DatabaseReference messageListener;
   // private List<FirebaseMessage> conversation;

    @Override
    public void onBackPressed() {
        if (this.messageListener != null) {
            //disconnect
        }
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        //////////////
        //Toolbar toolbar = (Toolbar)this.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        messagesListView = (ListView) this.findViewById(R.id.message_listview);
        Intent intent = getIntent();

        this.selectedUserMeta = intent.getParcelableExtra(Utils.SELECTED_USER);
        this.userMeta = intent.getParcelableExtra(Utils.USER);


       this.loadConversation(this.userMeta, this.selectedUserMeta);
        //firebase url: client_messages/client_id/sty_id {list of messages}
        // url for stylist: stylist_messages/sty_id/client_id/{list of msgs}

        user_bitmap = this.loadUserImage();
        selectedUserBitmap = this.loadSelectedUserImage();




       /* final String path = "client_messages/" + this..client_id() + "/" + meta.stylist_id();//
        final String path_sty = "stylist_messages/" + meta.stylist_id() + "/" + meta.client_id();
        final DatabaseReference sty_ref = FirebaseDatabase.getInstance().getReference().child(path_sty);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
       */
        ////CREATE METADATA for stylist and client if it does not exist already

        final AutoCompleteTextView msg = (AutoCompleteTextView) this.findViewById(R.id.message_autotext_field);

        /*msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    String
                   if(isValidMessage())
                    return true;
                }
                return false;
            }
        });*/
        Button send = (Button)this.findViewById(R.id.buttonSEND);
        send.setOnClickListener(new View.OnClickListener() {
            private boolean isValidMessage(String message, AutoCompleteTextView msg){
                if(message.length()==0){
                    return false;//msg.setError("");
                }else if (message.length()>250) {
                    msg.setError("Maximum characters allowed is 250.");
                    return false;
                }
                return true;
            }
            @Override
            public void onClick(View view) {
                String message = msg.getText().toString();
                if(isValidMessage(message,msg)){
                    FirebaseMessage fire_msg = new FirebaseMessage(message,userMeta.getId());
                    MessagingActivity.this.sendMessage(userMeta, fire_msg, selectedUserMeta);
                    MessagingActivity.this.sendMessage(selectedUserMeta, fire_msg, userMeta);
                    msg.setError(null);
                    msg.setText(null);
                }
            }
        });

    }////end on create



    /**
     * Creates a copy of the message. User signs a copy of the message as To sender. And the program
     * @param fromUser
     * @param firebase_message
     * @param toSelectedUser
     */
    @Override
    public void sendMessage(IMessagingMetaData fromUser, final FirebaseMessage firebase_message, IMessagingMetaData toSelectedUser) {

        String path = "messages/"+fromUser.getId()+"/"+toSelectedUser.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                GenericTypeIndicator<List<FirebaseMessage>> gti = new GenericTypeIndicator<List<FirebaseMessage>>() {};
                List<FirebaseMessage> messages ;
                if(mutableData.getValue() == null){
                        messages = new ArrayList<FirebaseMessage>();
                }else{
                    messages = mutableData.getValue(gti);
                }
                Collections.sort(messages);
                messages.add(firebase_message);
                mutableData.setValue(messages);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    Log.e("message","msg confirmed. on complete.");
            }
        });
        String meta_data = "message_meta_data/"+fromUser.getId();
        DatabaseReference meta_ref = FirebaseDatabase.getInstance().getReference().child(meta_data);
        meta_ref.setValue(new FirebaseInboxMetaData(fromUser));



    }

    /**
     * This method will query firebase database and load all messages from- messages/{user.ID}/{List<FirebaseMessages>}
     * If path doesnt exist then no messages are to be displayed. Otherwise, display all messages from the databaseReference.
     * This method also sets a databaseReference for the messages. Will terminate database listener once the activity is exited.
     *
     * @param user
     * @param selectedUser
     * @return
     */
    @Override
    public List<FirebaseMessage> loadConversation(IMessagingMetaData user, IMessagingMetaData selectedUser) {
        Log.e("In messaging app", "IS NULL? INTERFACE user: "+Boolean.valueOf(user==null)+" selectedUser: "+Boolean.valueOf(selectedUser==null));

        this.userMeta = (FirebaseMessagingUserMetaData) user;
        this.selectedUserMeta = (FirebaseMessagingUserMetaData) selectedUser;
        Log.e("In messaging app", "IS NULL? user: "+Boolean.valueOf(userMeta==null)+" selectedUser: "+Boolean.valueOf(selectedUserMeta==null));

       // this.conversation = new ArrayList<>();
        String path = "messages/" + user.getId() + "/" + selectedUser.getId().toString();
        this.messageListener = FirebaseDatabase.getInstance().getReference().child(path);
        this.messageListener.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {//no messages
                    return;
                }
                GenericTypeIndicator<List<FirebaseMessage>> gti = new GenericTypeIndicator<List<FirebaseMessage>>() {
                };
               List<FirebaseMessage> messages = dataSnapshot.getValue(gti);
                MessagingActivity.this.displayMessages(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error","firebase error cancelled...");
            }
        });

        return null;
    }

    /**
     * Sets the listadapter for listview as List<FirebaseMessages>.
     *
     * @param list
     */
    private void displayMessages(List<FirebaseMessage> list) {
        MessagingListAdapter adapter = null;
        if (this.messagesListView == null) {
            messagesListView = (ListView) this.findViewById(R.id.message_listview);
        } else if (this.messagesListView.getAdapter() != null) {//already an adapter
            adapter = (MessagingListAdapter) messagesListView.getAdapter();
            adapter.clear();
            adapter.addAll(list);
           // adapter.notifyDataSetChanged();
        } else {
           adapter = new MessagingListAdapter(this, list, userMeta, selectedUserMeta, user_bitmap, selectedUserBitmap);
            messagesListView.setAdapter(adapter);
        }
        messagesListView.setSelection(adapter.getCount()-1);

    }


    @Override
    public Bitmap loadSelectedUserImage() {
        return Utils.getBitmapFromDisk(Utils.SELECTED_USER);//gets bitmap from file and deletes file
    }

    @Override
    public Bitmap loadUserImage() {
        return Utils.getBitmapFromDisk(Utils.USER);//gets bitmap from file and deletes file
    }
    @Override
    public IMessagingMetaData getUser() {
        return this.userMeta;
    }

    @Override
    public IMessagingMetaData getSelectedUser() {
        return this.selectedUserMeta;
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

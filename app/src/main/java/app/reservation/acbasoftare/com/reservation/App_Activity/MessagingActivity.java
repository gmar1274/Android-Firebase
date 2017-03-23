package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
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

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.App_Objects.StylistMessageMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;
import app.reservation.acbasoftare.com.reservation.Interfaces.MessagingMetaData;
import app.reservation.acbasoftare.com.reservation.ListAdapters.UserViewMessagesInboxAdapter;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

public class MessagingActivity extends Activity {
    private Bitmap user_bitmap;
    private ListView lv;///////this is the messaging list view
    private Bitmap sty_bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //////////////
        Intent intent = getIntent();
        StylistMessageMetaData sty_meta = intent.getParcelableExtra("StylistMessageMetaData");
        final UserMessageMetaData usr_meta = intent.getParcelableExtra("UserMessageMetaData");
        ;
        final MessagingMetaData meta = new MessagingMetaData(usr_meta, sty_meta);//will determine which object to use....one will be null

        //firebase url: client_messages/client_id/sty_id {list of messages}
        // url for stylist: stylist_messages/sty_id/client_id/{list of msgs}

         user_bitmap = Utils.getBitmapFromDisk("user");//gets bitmap from file and deletes file
         sty_bitmap = Utils.getBitmapFromDisk("sty");

         lv = (ListView) this.findViewById(R.id.message_listview);


        final String path = "client_messages/" + meta.client_id() + "/" + meta.stylist_id();//
        final String path_sty = "stylist_messages/" + meta.stylist_id() + "/" + meta.client_id();
        final DatabaseReference sty_ref = FirebaseDatabase.getInstance().getReference().child(path_sty);
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(path);
        if (usr_meta != null) {
            createValueListener(ref,meta.client_id());
        }////////////case where the client is messaging...so add a listener to display
        else {
           createValueListener(sty_ref,meta.stylist_id());
        }/////////////OTHERWISE this is the stylist messaging........

        ////CREATE METADATA for stylist and client if it does not exist already

        final AutoCompleteTextView msg = (AutoCompleteTextView) this.findViewById(R.id.message_autotext_field);
        msg.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEND) {
                    if(usr_meta!=null){
                        sendMessage(ref,msg.getText().toString(), meta.client_id()); //send message as client
                    }else {
                        sendMessage(sty_ref, msg.getText().toString(), meta.stylist_id());//send as stylist
                    }return true;
                }
                return false;
            }

            /**
             * create the necessary objects to prep  a conccurent transaction to the specified database.
             * Send message as sender id
             * @param ref_param
             * @param msg
             * @param sender_id
             */
            private void sendMessage(DatabaseReference ref_param, String msg, String sender_id) {
                final FirebaseMessage fm = new FirebaseMessage(msg, sender_id);
                updateDatabase(ref_param,fm);
            }


        });


    }////end on create

    /**
     * Create a listener to update the GUI as the receiver.
     * @param ref_param
     * @param receiver_id
     */
    private void createValueListener(DatabaseReference ref_param, final String receiver_id) {
        ref_param.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FirebaseMessage> message_list = null;
                GenericTypeIndicator<List<FirebaseMessage>> gti = new GenericTypeIndicator<List<FirebaseMessage>>() {
                };
                if (dataSnapshot.getValue() == null) {
                    message_list = new ArrayList<FirebaseMessage>();
                } else {
                    message_list = dataSnapshot.getValue(gti);
                }
                Collections.sort(message_list);//sort by timestamp
                UserViewMessagesInboxAdapter ad = null;
                if (lv.getAdapter() == null) {
                    ArrayList<Bitmap> bm = new ArrayList<Bitmap>();
                    bm.add(user_bitmap);
                    bm.add(sty_bitmap);
                    ad = new UserViewMessagesInboxAdapter(MessagingActivity.this, message_list, bm, receiver_id);
                } else {
                    ad = (UserViewMessagesInboxAdapter) lv.getAdapter();
                    ad.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("message err", "message errors on inbox ...");
            }
        });
    }

    /**
     * This method will send a conccurrent transaction to the provided database with the message as the value
     * @param ref
     * @param fm
     */
    private void updateDatabase(DatabaseReference ref, final FirebaseMessage fm) {
        ref.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                List<FirebaseMessage> list = null;
                GenericTypeIndicator<List<FirebaseMessage>> gti = new GenericTypeIndicator<List<FirebaseMessage>>() {
                };
                if (mutableData.getValue() == null) {
                    list = new ArrayList<FirebaseMessage>();
                } else {
                    list = mutableData.getValue(gti);
                }
                if (!list.contains(fm)) {
                    list.add(fm);
                }
                mutableData.setValue(list);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.e("On complete", "In ref for messaging");
            }
        });//////////end user messg update
    }

}

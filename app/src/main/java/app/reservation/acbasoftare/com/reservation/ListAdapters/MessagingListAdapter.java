package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.Interfaces.IConversation;
import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2017-03-22.
 * ADAPTER THAT WILL DISPLAY THE MESSAGES FOR CLIENT STYLIST COMMUNICATIONS
 */
public class MessagingListAdapter extends ArrayAdapter<FirebaseMessage> implements IConversation {
    //private ArrayList<Bitmap> images;//0 = user and 1 == selectedUser
    //private  String receiver;
    private IMessagingMetaData user, selectedUser;
    private Bitmap userBM, selectedBM;
    private List<FirebaseMessage> messages;
    public MessagingListAdapter(Context context, List<FirebaseMessage> objects,IMessagingMetaData user, IMessagingMetaData selectedUser, Bitmap userBM,Bitmap selectedBM){//,ArrayList<Bitmap> bm,String receiver) {
        super(context, R.layout.message_user_view_meta_data, objects);
        //this.images = bm;
        //this.receiver = receiver;
        this.user = user;
        this.selectedUser = selectedUser;
        this.userBM = userBM;
        this.selectedBM = selectedBM;
        this.messages = objects;
    }

@Override
public int getCount(){
    return this.messages.size();
}
    @Override
    public View getView(final int position_item, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        // Creating store_list view of row.
        FirebaseMessage msg = getItem(position_item);
        View rootView = inflater.inflate(R.layout.messaging_fragment_layout, parent, false);
        ImageView rec = (ImageView)rootView.findViewById(R.id.reciever_imageView);
        ImageView sender = (ImageView)rootView.findViewById(R.id.sender_imageView);
        ConstraintLayout sender_lay = (ConstraintLayout)rootView.findViewById(R.id.constraintLayout_sender);
        ConstraintLayout rec_lay = (ConstraintLayout)rootView.findViewById(R.id.constraintLayout_rec);
        TextView rec_tv = (TextView)rootView.findViewById(R.id.receiver_textfield);
        TextView sender_tv = (TextView)rootView.findViewById(R.id.sender_textfield);
        TextView rec_timestamp_tv = (TextView)rootView.findViewById(R.id.receiver_timestamp_textfield);
        TextView sender_timestamp_tv = (TextView)rootView.findViewById(R.id.sender_timestamp_textfield);
        if(msg.getSender_id().equals(this.user.getId())){ //if its user sent this message
            sender.setVisibility(View.GONE);
            sender_lay.setVisibility(View.GONE);
            rec_tv.setText(msg.getMessage());
            rec_timestamp_tv.setText(msg.getTimestamp());
            rec.setImageBitmap(userBM);
        }else {
            rec_lay.setVisibility(View.GONE);
            rec.setVisibility(View.GONE);
            sender_tv.setText(msg.getMessage());
            sender_timestamp_tv.setText(msg.getTimestamp());
            sender.setImageBitmap(selectedBM);
        }
        return rootView;
    }


    @Override
    public IMessagingMetaData getUser() {
        return this.user;
    }

    @Override
    public IMessagingMetaData getSelectedUser() {
        return this.selectedUser;
    }
}
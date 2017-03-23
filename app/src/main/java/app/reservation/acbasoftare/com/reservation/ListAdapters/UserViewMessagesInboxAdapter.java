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

import java.util.ArrayList;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2017-03-22.
 * ADAPTER THAT WILL DISPLAY THE MESSAGES FOR CLIENT STYLIST COMMUNICATIONS
 */
public class UserViewMessagesInboxAdapter extends ArrayAdapter<FirebaseMessage> {
    private ArrayList<Bitmap> images;//0 = recienver and 1 == sender
    private  String receiver;
    public UserViewMessagesInboxAdapter(Context context, List<FirebaseMessage> objects,ArrayList<Bitmap> bm,String receiver) {
        super(context, R.layout.message_user_view_meta_data, objects);
        this.images = bm;
        this.receiver = receiver;
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
        if(msg.getSender_id().equals(this.receiver)){ //if its relative reciever or if message was sent by receiver
            sender.setVisibility(View.GONE);
            sender_lay.setVisibility(View.GONE);
            rec_tv.setText(msg.getMessage());
            rec_timestamp_tv.setText(msg.getTimestamp());
            rec.setImageBitmap(images.get(0));
        }else {
            rec_lay.setVisibility(View.GONE);
            rec.setVisibility(View.GONE);
            sender_tv.setText(msg.getMessage());
            sender_timestamp_tv.setText(msg.getTimestamp());
            sender.setImageBitmap(images.get(1));
        }
        return rootView;
    }


}

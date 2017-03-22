package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2017-03-22.
 */
public class UserViewMessagesInboxAdapter extends ArrayAdapter<FirebaseMessage> {
    public UserViewMessagesInboxAdapter(Context context, List<FirebaseMessage> objects) {
        super(context, R.layout.message_user_view_meta_data, objects);
    }


    @Override
    public View getView(final int position_item, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        // Creating store_list view of row.
        FirebaseMessage msg = getItem(position_item);
        View rootView = inflater.inflate(R.layout.message_user_view_meta_data, parent, false);



        return rootView;
    }


}

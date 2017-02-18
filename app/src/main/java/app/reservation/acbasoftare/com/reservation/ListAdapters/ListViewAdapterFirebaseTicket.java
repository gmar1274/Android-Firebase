package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 2017-02-17.
 */
public class ListViewAdapterFirebaseTicket extends ArrayAdapter<Ticket> {
    public ListViewAdapterFirebaseTicket(Context context, int resource, List<Ticket> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Ticket t = getItem(position);
        LayoutInflater i = LayoutInflater.from(this.getContext());
        View view = i.inflate(R.layout.list_view_ticket_item,null,false);
        TextView name = (TextView)view.findViewById(R.id.tv_ticket_name);
        name.setText(t.getName().toUpperCase());
        TextView unique = (TextView)view.findViewById(R.id.tv_unique_number);
        unique.setText(String.valueOf(t.unique_id));
        TextView sty = (TextView)view.findViewById(R.id.tv_stylist_name);
        sty.setText(t.stylist.toUpperCase());
        TextView readyBy = (TextView) view.findViewById(R.id.tv_readyby_mainqueue);
        readyBy.setText(t.readyBy);

        return view;
    }
}

package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.DateViewerActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.EmployeeActivity;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 12/16/16.
 */
public class DateArrayAdapter extends ArrayAdapter<String> {
private EmployeeActivity ea;
        public DateArrayAdapter(EmployeeActivity ea, int resource, ArrayList<String> objects) {
            super(ea.getApplicationContext(), resource, objects);
            this.ea = ea;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            //final SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
            //final Date date = getItem(position);
            final String date = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.expandable_list_item, parent, false);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.text_view_expandable_child);
            tv.setTextSize(36);
            tv.setTypeface(null,Typeface.BOLD);
            tv.setText(date);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getContext(),DateViewerActivity.class);
                    i.putExtra("date",date);
                    ea.startActivity(i);
                }
            });
            return convertView;
        }
    }

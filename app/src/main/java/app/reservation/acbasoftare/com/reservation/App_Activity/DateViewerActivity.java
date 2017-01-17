package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.ReservationDetails;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;
import app.reservation.acbasoftare.com.reservation.ExpandableListView.ExpandableListViewAdapter;
import app.reservation.acbasoftare.com.reservation.R;


public class DateViewerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_employee_today_layout);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        try {
            ProgressBar pb= (ProgressBar)findViewById(R.id.progressBar_emp_act);
            pb.setVisibility(View.GONE);
            Date date = sdf.parse(getIntent().getStringExtra("date"));
            TextView tv = (TextView)findViewById(R.id.textView_stylist_today_upcoming_header);
            tv.setText(tv.getText()+sdf.format(date));
            ArrayList<TimeSet> headers = EmployeeActivity.reservation.getDayTimes(date);
            HashMap<TimeSet,ReservationDetails> child = EmployeeActivity.reservation.getReservationDetailsHashMap();
            ExpandableListViewAdapter lva = new ExpandableListViewAdapter(this,headers,child);
            ExpandableListView lv = (ExpandableListView)findViewById(R.id.expandable_List_view);
            lv.setAdapter(lva);
        } catch(ParseException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed(){
        finish();
    }
}

package app.reservation.acbasoftare.com.reservation.App_Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.ParseException;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.Ticket;
import app.reservation.acbasoftare.com.reservation.R;

import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.AD_AGE_DATE_STRING;
import static app.reservation.acbasoftare.com.reservation.App_Activity.LoginActivity.sdf;


public class TicketHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_history);
        AdView a = (AdView) this.findViewById(R.id.adView_TicketHistory);
        AdRequest adRequest = null;
        if(MainActivity.ADTESTING){
            adRequest  = new AdRequest.Builder().addTestDevice("23B075DED4F5E3DB63757F55444BFF46").build();
        }
        else{
            try {
                adRequest  = new AdRequest.Builder().setBirthday(sdf.parse(AD_AGE_DATE_STRING)).build();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        //Log.e("ADRFEQQQQ",String.valueOf(adRequest==null));
       // Log.e("ADVIEW:: ",a.isShown()+" <<<< "+a.isLoading());
        a.loadAd(adRequest);

        ArrayList<Ticket> ticket_history = this.getIntent().getParcelableArrayListExtra("ticket_history");
        ListView lv = (ListView)this.findViewById(R.id.ticket_history_listview);
        ListAdapterTicket la = new ListAdapterTicket(this,R.layout.ticket_history_listview_item,ticket_history);
        lv.setAdapter(la);
        //Log.e("Size list:",ticket_history.size()+"");
    }
    @Override
    public void onBackPressed(){
        this.finish();
    }
    private class ListAdapterTicket extends ArrayAdapter<Ticket> {
        private ArrayList<Ticket> list;
        public ListAdapterTicket(Context context, int resource,ArrayList<Ticket> objects) {
            super(context, resource, objects);
            this.list = objects;
        }
        @Override
        public int getCount(){
            return this.list.size();
        }
        public View getView(final int position_item, View convertView, ViewGroup parent) {

            if(convertView==null) {
                LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.ticket_history_listview_item, parent, false);
            }
            TextView num = (TextView)convertView.findViewById(R.id.th_number_textView);
            TextView sty = (TextView)convertView.findViewById(R.id.th_stylist_textView);
            Ticket t = this.getItem(position_item);

            num.setText("Your Ticket #: "+t.getUnique_id());
            sty.setText("Stylist: "+t.getStylist().toUpperCase());
            return convertView;
        }
    }
}
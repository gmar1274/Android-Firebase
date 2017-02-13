package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 2016-12-04.
 */
public class StylistArrayAdapter extends ArrayAdapter<Stylist> {
    /**
     * I MADE CHANGES TO THE contect c. MIGHT BE BUG.
     * Before it was MainActivity.a.getResouces()...
     * but i changed it to get c.getResources...
     */
    private Context c;
    public StylistArrayAdapter(Context context, int resource, ArrayList<Stylist> objects) {
        super(context, resource, objects);
        this.c = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Stylist s = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rv_stylist, parent, false);
        }
        RadioButton r = (RadioButton) convertView.findViewById(R.id.rb_rv_stylist);
        //When clicked on the stylist this method should populate the give stylist appointments on the calendarView
       // r.setButtonDrawable(new BitmapDrawable(c.getResources(), Utils.convertBytesToBitmap(Utils.convertToByteArray(s.getImage_bytes()))));
        r.setText(s.getName().toUpperCase());
        r.setOnClickListener(new View.OnClickListener() {//when
            @Override
            public void onClick(View view) {
                notifyDataSetChanged();
            }
        });

   /***     QuickContactBadge q = (QuickContactBadge) convertView.findViewById(R.id.quickContactBadge); // ImageView iv = (ImageView) convertView.findViewById(R.id.stylist_image);
        if (s.getImage() == null) {
            q.setImageResource(R.drawable.acba);
        } else {
            q.setImageBitmap(s.getImage());
            q.assignContactFromPhone(s.getPhone(),true);
        }
        q.setMode(ContactsContract.QuickContact.MODE_SMALL);
        // iv.setVisibility(View.VISIBLE);
*/
        return convertView;
    }
}

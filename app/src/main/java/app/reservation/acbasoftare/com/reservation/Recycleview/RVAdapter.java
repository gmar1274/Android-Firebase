package app.reservation.acbasoftare.com.reservation.Recycleview;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 12/6/16.
 * A generic recycleview adapter. Of list Generic.
 */
public class RVAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int CURSOR = 0;
    public static int SERVICE_CURSOR = 0;
    private ArrayList<E> list;
    private int layout;
    private boolean stylist;

    public RVAdapter(ArrayList<E> list, int layout, boolean stylist) {
        this.list = list;
        this.layout = layout;
        this.stylist = stylist;
    }

    public Stylist getStylist() {
        return (Stylist) list.get(CURSOR);
    }

    public SalonService getSalonService() {
        return (SalonService) list.get(SERVICE_CURSOR);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ViewHolder(v, stylist);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {
        final ViewHolder holder = (ViewHolder) vh;
        RadioButton rb = holder.rb;
        rb.setTag(position);
        if (holder.stylist) {
            rb.setChecked(position == CURSOR);
        } else {
            rb.setChecked(position == SERVICE_CURSOR);
        }
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.stylist) {
                    CURSOR = position;
                } else {
                    SERVICE_CURSOR = position;
                }
                notifyDataSetChanged();
            }
        });
        if (holder.stylist) {
            Stylist s = (Stylist) list.get(position);
            rb.setText(s.getName().toUpperCase());
            Drawable d = new BitmapDrawable(MainActivity.a.getResources(), Utils.resize(Utils.convertBytesToBitmap(s.getImage_bytes()),80,80));
            rb.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);

        } else {//do service
            DecimalFormat df = new DecimalFormat("$ ###,##0.00");
            SalonService s = (SalonService) list.get(position);
            rb.setText("Service: " + s.getName() + "\n" + "Price: " + df.format(s.getPrice()) + "\nService Duration: " + s.getDuration());
        }
    }

public  void customeNotifyDataSetChanged(){
    this.notifyDataSetChanged();
}
    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //  public QuickContactBadge q;
        public RadioButton rb;
        public boolean stylist;
        // private RadioGroup rg;

        public ViewHolder(View itemView, boolean stylist) {
            super(itemView);
            this.stylist = stylist;
            if (stylist) {
                // q = (QuickContactBadge) itemView.findViewById(R.id.quickContactBadge);
                // rg = new RadioGroup(itemView.getContext());
                rb = (RadioButton) itemView.findViewById(R.id.rb_rv_stylist);
            } else {
                rb = (RadioButton) itemView.findViewById(R.id.rb_rv_service);
            }

        }
    }
}

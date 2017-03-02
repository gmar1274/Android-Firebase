package app.reservation.acbasoftare.com.reservation.ExpandableListView;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.ReservationDetails;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;
/**
 * Created by user on 12/13/16.
 */
public class ExpandableListViewAdapter<K> extends BaseExpandableListAdapter{
    private final Context context;
    private List<K> list_header;////DateTime 5-6 etc...
    private HashMap<K,ReservationDetails> child;///just a text view listing details

    public ExpandableListViewAdapter(Context c,List<K> head, HashMap<K,ReservationDetails> child){
        this.list_header=head;//TimeSet
        this.child = child;
        this.context = c;

    }

    public HashMap<K,ReservationDetails> getDateTimeHashMap(){
        return  this.child;
    }
    public List<K> getTimeRanges(){
        return this.list_header;
    }
    @Override
    public int getGroupCount() {
        return this.list_header.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return this.list_header.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.child.get(this.list_header.get(i)) ;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String title = getGroup(i).toString();
        if(view==null){
            LayoutInflater inflate = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view  =inflate.inflate(R.layout.expandable_listview_header,null);
        }
        TextView tv = (TextView)view.findViewById(R.id.textview_expandable_header);
        tv.setTypeface(null,Typeface.BOLD);
        tv.setText(title);
        return view;
    }
    @Override
    public View getChildView(int grouppos, int childpos, boolean b, View view, ViewGroup viewGroup) {
            if(view==null){
                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.expandable_list_item,null);
            }
            TimeSet ts=(TimeSet) list_header.get(grouppos);
            ReservationDetails rd =(ReservationDetails) child.get(ts);
            TextView tv= (TextView) view.findViewById(R.id.text_view_expandable_child);
           if(rd!=null) tv.setText("Client Name: "+rd.customer.getName()+"\n"+"Client's contact info: "+ Utils.formatPhoneNumber(rd.customer.getPhone())+"\nService Requested: "+rd.service_name+"\n"+"Comments: "+rd.notes);
        return view;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
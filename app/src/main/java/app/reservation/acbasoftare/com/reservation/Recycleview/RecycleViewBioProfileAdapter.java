package app.reservation.acbasoftare.com.reservation.Recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 6/13/17.
 *
 */
 public class RecycleViewBioProfileAdapter<E> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<E> list;
        private int layout;
        private Context c;

    /**
     *
     * @param list the file location on phone...
     * @param layout layout ID
     */
        public RecycleViewBioProfileAdapter(Context c, ArrayList<E> list, int layout) {
            this.list = list;
            this.layout = layout;
            this.c = c;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder vh, final int position) {
           ViewHolder holder = (ViewHolder) vh;
            holder.iv.setImageBitmap(Utils.decodeFileToBitmap(c, (String) list.get(position) ));
        }

        public  void customeNotifyDataSetChanged(){
            this.notifyDataSetChanged();
        }
        @Override
        public int getItemCount() {
            return this.list.size();
        }

    /**
     * Layout of res/activity_stylist_bio_scrolling.xml
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView iv;
            public ViewHolder(View itemView) {
                super(itemView);
                this.iv = (ImageView)itemView.findViewById(R.id.imageView);
            }
        }

}

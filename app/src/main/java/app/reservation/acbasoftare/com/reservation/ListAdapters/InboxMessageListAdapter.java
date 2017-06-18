package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.CircleImage;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseInboxMetaData;
import app.reservation.acbasoftare.com.reservation.Interfaces.IFirebaseMessagingInbox;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 2017-03-21.
 * ADPATER to diplay the inbox view.
 *
 */
public class InboxMessageListAdapter extends ArrayAdapter<FirebaseInboxMetaData> {

   // private HashMap<String,CircleImage> images_downloaded;
    private ArrayList<FirebaseInboxMetaData> list;
    private HashMap<String,String> file_loc;

    public InboxMessageListAdapter(Context c, ArrayList<FirebaseInboxMetaData> list, HashMap<String,String> file_loc){
        super(c, R.layout.inbox_message_view_layout,list);
       // this.images_downloaded = new HashMap<>();
        this.list = list;
        this.file_loc = file_loc;
    }

    public  Bitmap getBitmapStylist(String id){
        //if(this.images_downloaded.get(id)==null)return null;
       // return this.images_downloaded.get(id).getBitmap();
        return Utils.decodeFileToBitmap(getContext(),this.file_loc.get(id));
    }
    public FirebaseInboxMetaData getMessage(int pos){
        return this.list.get(pos);
    }
    @Override
    public View getView(final int position_item, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        // Creating store_list view of row.

        final IFirebaseMessagingInbox metaData = getItem(position_item);
        Log.e("\nHM KEYs: ",file_loc.keySet().toString());
        Log.e("HM V's: ", file_loc.values().toString());
        Log.e("Debug LIST: ",list.toString());
        Log.e("Debug POS: ","pos item: ["+metaData.getId()+", "+metaData.getName()+" ]\n");


        View rootView = inflater.inflate(R.layout.inbox_message_view_layout, parent, false);
        TextView sty_name = (TextView) rootView.findViewById(R.id.stylist_name_textfield);
        TextView store_name = (TextView) rootView.findViewById(R.id.store_name_textfield);
        ConstraintLayout notification = (ConstraintLayout)rootView.findViewById(R.id.message_notification_user_view_layout);
        if(metaData.isRead()==false){
            notification.setVisibility(View.VISIBLE);
        }else{
            notification.setVisibility(android.view.View.GONE);
        }
        final ImageView sty_img = (ImageView)rootView.findViewById(R.id.stylist_imageView);
        sty_name.setText(metaData.getName().toUpperCase());
        store_name.setText("");
        Bitmap bm = Utils.decodeFileToBitmap(getContext(),file_loc.get(metaData.getId()));
        //this.images_downloaded.get(position_item);
            if (bm != null) {
                CircleImage ci  = new CircleImage(bm);
                sty_img.setImageDrawable(ci);
            }else{////either the firebase hasnt loaded the stylist from store yet so fetch and check if exists from storage bucket
                sty_img.setImageDrawable(new CircleImage(Utils.setDefaultBitmap(getContext())));//setImageBitmap(Utils.setDefaultBitmap(getContext()));
                String path_formatted=metaData.getImage_storage_path().replace(".png","");

                StorageReference ref = FirebaseStorage.getInstance().getReference().child(path_formatted);
                try {
                    final File temp = File.createTempFile(metaData.getId(),Utils.EXT);
                    ref.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Log.e("File","File CREATED!!!fetched success..");//File temp path: "+temp.getAbsolutePath());
                            file_loc.put(metaData.getId(),temp.getAbsolutePath());
                            notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Womp","File failed to created...no image exists...fbase ID: "+metaData.getImage_storage_path());
                        //e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                Log.e("File err","File error creating on adapter...");
                e.printStackTrace();
                }
            }
            return rootView;
    }
    @Override
    public void add(FirebaseInboxMetaData obj){
        if(this.list.contains(obj))return;
        this.list.add(obj);
        this.notifyDataSetChanged();
    }
    public ArrayList<FirebaseInboxMetaData> getList(){
        return this.list;
    }

    public FirebaseInboxMetaData getMetaData(String id) {
        int index = this.list.indexOf(new FirebaseInboxMetaData(id));
        if(index<0)return null;
        return this.list.get(index);
    }
    public void remove(int position){
        this.list.remove(position);
        this.notifyDataSetChanged();
    }
    public FirebaseInboxMetaData getMetaData(int position) {
        return this.list.get(position);
    }

    public void update(FirebaseInboxMetaData inbox_meta) {
        int i = list.indexOf(inbox_meta);
        list.remove(i);
        list.add(i,inbox_meta);
        notifyDataSetChanged();
    }
}

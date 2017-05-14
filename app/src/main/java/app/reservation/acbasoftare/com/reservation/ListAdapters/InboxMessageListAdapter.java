package app.reservation.acbasoftare.com.reservation.ListAdapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.CircleImage;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseInboxMetaData;
import app.reservation.acbasoftare.com.reservation.Interfaces.IFirebaseMessagingInbox;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 2017-03-21.
 */
public class InboxMessageListAdapter extends ArrayAdapter<FirebaseInboxMetaData> {

   // private HashMap<String,CircleImage> images_downloaded;
    private ArrayList<FirebaseInboxMetaData> list;
    private HashMap<String,String> file_loc;

    public InboxMessageListAdapter(Context c, ArrayList<FirebaseInboxMetaData> list, HashMap<String,String> file_loc){
        super(c, R.layout.message_user_view_meta_data,list);
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
        View rootView = inflater.inflate(R.layout.message_user_view_meta_data, parent, false);
        TextView sty_name = (TextView) rootView.findViewById(R.id.stylist_name_textfield);
        TextView store_name = (TextView) rootView.findViewById(R.id.store_name_textfield);
        ConstraintLayout notification = (ConstraintLayout)rootView.findViewById(R.id.message_notification_user_view_layout);
        if(metaData.isRead()==false){
            notification.setVisibility(View.VISIBLE);
        }else{
            notification.setVisibility(android.view.View.GONE);
        }
        final ImageView sty_img = (ImageView)rootView.findViewById(R.id.stylist_imageView);
        //if(position_item+1 <= this.images_downloaded.size()){
            //them theres an image for it no need to download....\
        sty_name.setText(metaData.getName().toUpperCase());
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child()
        store_name.setText("");


       // Log.e("ADAPTER: ","CI: "+file_loc.get(metaData.getId())+" META: "+metaData);
        //if(sty_img.getDrawable()!=null){Log.e("NOT NULL","CI");return rootView;}

        CircleImage ci  = new CircleImage(Utils.decodeFileToBitmap(getContext(),file_loc.get(metaData.getId())));//this.images_downloaded.get(position_item);
            if (ci != null) {
                sty_img.setImageBitmap(ci.getBitmap());
            }else{
                sty_img.setImageBitmap(Utils.setDefaultBitmap(getContext()));
            }
        //}else {/////////////else the adapter has not loaded the stys images yet. Other words this is the first time this adapter has been called


            /*StorageReference ref = FirebaseStorage.getInstance().getReference().child(metaData.getImage_storage_path());
            try {
                final File temp = File.createTempFile(metaData.getId(),Utils.EXT);
                ref.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.e("File","File CREATED!!! File temp path: "+temp.getAbsolutePath());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Womp","File failed to created...");
                        e.printStackTrace();
                    }
                });
            } catch (IOException e) {
                Log.e("File err","File error creating on adapter...");
                e.printStackTrace();
            }*/

            /*final StorageReference sr = FirebaseStorage.getInstance().getReference().getRoot().child(metaData.getImage_storage_path());
            sr.getBytes(MainActivity.IMAGE_DOWNLOAD_LIMIT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = Utils.convertBytesToBitmap(bytes);
                    CircleImage ci = new CircleImage(bm);
                    sty_img.setImageBitmap(ci.getBitmap());
                    images_downloaded.put(metaData.getId(),ci);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.e("Fire err. ","Error on image loactiom...not there .."+"\nPath: "+sr.getPath());
                   // images_downloaded.add(position_item,null);
                    images_downloaded.put(metaData.getId(),null);
                }
            });*/
        //}
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

    public boolean contains(String key) {
        for(FirebaseInboxMetaData meta:this.list){
            if(meta.getId().equals(key))return true;
        }
        return false;
    }
}

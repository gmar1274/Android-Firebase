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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.CircleImage;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

import static android.support.design.R.styleable.View;

/**
 * Created by user on 2017-03-21.
 */
public class UserMessageMetaDataAdapter extends ArrayAdapter<UserMessageMetaData> {

    private ArrayList<CircleImage> images_downloaded;
    public  UserMessageMetaDataAdapter(Context c, ArrayList<UserMessageMetaData> list){
        super(c, R.layout.message_user_view_meta_data,list);
        this.images_downloaded = new ArrayList<>();
    }

    public  Bitmap getBitmapStylist(int pos){
        if(this.images_downloaded.size()<= pos)return null;
        return this.images_downloaded.get(pos).getBitmap();
    }
    @Override
    public View getView(final int position_item, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        // Creating store_list view of row.
        UserMessageMetaData metaData = getItem(position_item);
        View rootView = inflater.inflate(R.layout.message_user_view_meta_data, parent, false);
        TextView sty_name = (TextView) rootView.findViewById(R.id.stylist_name_textfield);
        TextView store_name = (TextView) rootView.findViewById(R.id.store_name_textfield);
        ConstraintLayout notification = (ConstraintLayout)rootView.findViewById(R.id.message_notification_user_view_layout);
        notification.setVisibility(android.view.View.GONE);
        final ImageView sty_img = (ImageView)rootView.findViewById(R.id.stylist_imageView);
        if(position_item+1 <= this.images_downloaded.size()){
            //them theres an image for it no need to download....
            CircleImage ci  = this.images_downloaded.get(position_item);
            if (ci != null) {
                sty_img.setImageBitmap(ci.getBitmap());
            }else{

            }
        }else {/////////////else the adapter has not loaded the stys images yet. Other words this is the first time this adapter has been called
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(metaData.getStylist_photo_uri());
            sr.getBytes(MainActivity.IMAGE_DOWNLOAD_LIMIT).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bm = Utils.convertBytesToBitmap(bytes);
                    CircleImage ci = new CircleImage(bm);
                    sty_img.setImageBitmap(ci.getBitmap());
                    images_downloaded.add(position_item,ci);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Log.e("Fire err. ","Error on image loactiom...not there ..");
                    images_downloaded.add(position_item,null);
                }
            });
        }
            return rootView;
    }
}

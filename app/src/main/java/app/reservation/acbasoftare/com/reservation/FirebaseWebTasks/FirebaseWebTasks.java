package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.MessagingActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.CircleImage;
import app.reservation.acbasoftare.com.reservation.App_Objects.CustomFBProfile;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessagingUserMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 1/16/17.
 */
public class FirebaseWebTasks {
    /**
     * This method uses TicketSceenActivity to save the image under root/store_id/images/stylists/filename
     *
     * @param bitmap
     * @param filename
     */
    public static void uploadImage(final Activity a, Bitmap bitmap, String filename) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bit = bitmap;
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] arr = stream.toByteArray();
        TicketScreenActivity tsa = (TicketScreenActivity) a;
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(tsa.store.getStore_number() + "/images/stylists/" + filename);//ticketScreenActivity
        UploadTask up = ref.putBytes(arr);
        up.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                //showToast(a,"Imaged failed to upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Storage File: ", "Success on upload");
                Toast.makeText(a, "Image successfully uploaded!", Toast.LENGTH_LONG).show(); // showToast(a,"Imaged success on upload");  //Uri downloadURL = taskSnapshot.getDownloadUrl();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(a, "Image failed uploaded...", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This methods will popluate the lists of both stylists and bitmaps. Bitmpas will contain the pictures from firebase storeage and
     * Stylist will contain the stylists from the store gathered by the query from firebase database.
     *
     * @param store
     * @param list_stylist
     * @param pd
     */
    public static void downloadImages(final TicketScreenActivity tsa, final FirebaseStore store, final ArrayList<Stylist> list_stylist, final ProgressDialog pd) {

        TicketScreenActivity.bitmaps.clear();
        // final TicketScreenActivity tsa = (TicketScreenActivity) pd.getContext();
        for (final Stylist s : list_stylist) {
            //Log.e("FIREBASE IMAGES STYLIS:",s.getId()+" ID AND NAME: "+s.getName());
            String name = s.getId();
            // if(name.equals("-1")){name = "-1.png";}
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(store.getPhone() + "/images/stylists/" + name);
            sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("OnSuccessImage", "image on success");
                    Bitmap b = Utils.convertBytesToBitmap(bytes);//Utils.convertToByteArray(s.getImage_bytes())); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                    TicketScreenActivity.bitmaps.put(s.getId(), b);
                    //bytes=null;
                    if (TicketScreenActivity.bitmaps.keySet().size() == list_stylist.size()) {//finished loading all images for the shop stylists
                        pd.dismiss();
                        tsa.loadTckets(pd);
                        Log.e("Loaded all images.", "sty images are all loaded..");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.e("ERROR", "IMAGE ERROR"); // Handle any errors
                    exception.printStackTrace();
                    pd.dismiss();
                }
            });
        }

    }

    /**
     * This method fetches the images and displays them on the View.
     *
     * @param store
     * @param list_stylist
     * @param pd
     */
    public static void downloadImages(final MainActivity ma, final FirebaseStore store, final ArrayList<Stylist> list_stylist, final ProgressDialog pd, final HashMap<String, String> stylist_bitmaps) {

        // final TicketScreenActivity tsa = (TicketScreenActivity) pd.getContext();
        for (final Stylist s : list_stylist) {
            //Log.e("FIREBASE IMAGES STYLIS:",s.getId()+" ID AND NAME: "+s.getName());
            String name = s.getId();
            StorageReference sr = FirebaseStorage.getInstance().getReference().child(store.getPhone() + "/images/stylists/" + name);
            try {
                Utils.createFileFromFirebaseToDevice(sr, s.getId(), stylist_bitmaps);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("error", "error from downLoadImages FWT class");
            }
           /* sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Log.e("OnSuccessImage","image on success");
                    // s.setImage_bytes(Utils.convertToString(bytes));
                    //bytes=null;
                    //list_stylist.add(s);
                    Bitmap b = Utils.convertBytesToBitmap(bytes); //Utils.decodeSampledBitmapFromArray(s.getImage_bytes(), 50, 50);
                    bytes=null;
                    if(stylist_bitmaps==null){stylist_bitmaps = new java.util.HashMap<>();}
                    stylist_bitmaps.put(s.getId(),b);
                    if(stylist_bitmaps.size() == list_stylist.size()){
                        //store.determineCurrentTicketAfterFirebaseLoads();
                        ma.updateStore(store);
                        ma.initializeStylists(list_stylist,stylist_bitmaps);
                        Log.e("FINISH: ","Finished download images");
                        pd.dismiss();
                        *//*ProgressBar pb = (ProgressBar)ma.rootView_LiveTab.findViewById(R.id.progressBar_live_feed);
                        pb.setVisibility(View.GONE);*//*
                    }
                    //not sure store gets updated will delete the use even though its stored in array cant useon the change but its ok because i can set the stylist list to the list for store
                    // pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    stylist_bitmaps = null;
                    //ma.initializeStylists(ma.stylists_list,null);
                    Log.e("ERROR","IMAGE ERROR"); // Handle any errors
                    exception.printStackTrace();
                    pd.dismiss();
                }
            });*/
        }

    }
}
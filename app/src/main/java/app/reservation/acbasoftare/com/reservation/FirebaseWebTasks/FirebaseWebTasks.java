package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;

import static app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity.mStorageRef;

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
    public static void uploadImage(Bitmap bitmap, String filename) {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        Bitmap bit=bitmap;
        bit.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] arr=stream.toByteArray();
        StorageReference ref=mStorageRef.child(TicketScreenActivity.store.getStore_number() + "/images/stylists/" + filename);//ticketScreenActivity
        UploadTask up=ref.putBytes(arr);
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
                // showToast(a,"Imaged success on upload");  //Uri downloadURL = taskSnapshot.getDownloadUrl();
            }
        });
    }
    public static void downloadImages(Store store, final ArrayList<Stylist> list, ProgressDialog pd){
       List<Stylist> arr = store.getStylistListFirebaseFormat();

        for(final Stylist s : arr ) {
           // Log.e("FIREBASE IMAGES STYLIS:",s.getID()+" ID AND NAME: "+s.getName());
            StorageReference sr = mStorageRef.child(store.getPhone()+ "/images/stylists/"+s.getId());
            sr.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                   // byte[] qrimageBytes = Base64.decode(bytes, Base64.DEFAULT);
                   // Bitmap bmp = BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length);//Utils.resize(BitmapFactory.decodeByteArray(qrimageBytes, 0,qrimageBytes.length),100,100);
                    //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
                    //ss.setBitmap(bmp); // Use the bytes to display the image
                    s.setImage_bytes(bytes);
                    list.add(s);
                   //not sure store gets updated on the change but its ok because i can set the stylist list to the list for store
                   // pd.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                   Log.e("ERROR","IMAGE ERROR"); // Handle any errors
                    exception.printStackTrace();
                   // pd.dismiss();
                }
            });
        }
        pd.dismiss();
    }

}

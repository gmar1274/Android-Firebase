package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import app.reservation.acbasoftare.com.reservation.App_Activity.TicketScreenActivity;

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
        StorageReference ref=mStorageRef.child(TicketScreenActivity.store_id + "/images/stylists/" + filename);//ticketScreenActivity
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
}

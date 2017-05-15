package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;
import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 4/25/17.
 * This class will contain the meta data for the messaging part of the app.
 * Meta data includes: NAME, PHOTO_URL, ID.
 * Firebase url: message_meta_data/{ID}/
 */

public class FirebaseMessagingUserMetaData implements Parcelable, IMessagingMetaData{
    private String id, image_storage_path, name;

    public FirebaseMessagingUserMetaData(FirebaseStore store, Stylist stylist){
        this.id = stylist.getId();
        this.image_storage_path = Utils.getStylistImagePath(store,stylist);
        this.name = stylist.getName();
    }
    public FirebaseMessagingUserMetaData(CustomFBProfile profile){
        this.id = profile.getId();
        this.image_storage_path = profile.getPic_url(); //.getUri().getPath();
        this.name = profile.getName();
    }
    public FirebaseMessagingUserMetaData(IMessagingMetaData selectedUser){
        this.id = selectedUser.getId();
        this.image_storage_path = selectedUser.getImage_storage_path();
        this.name = selectedUser.getName();
    }

    protected FirebaseMessagingUserMetaData(Parcel in) {
        id = in.readString();
        image_storage_path = in.readString();
        name = in.readString();
    }

    public static final Creator<FirebaseMessagingUserMetaData> CREATOR = new Creator<FirebaseMessagingUserMetaData>() {
        @Override
        public FirebaseMessagingUserMetaData createFromParcel(Parcel in) {
            return new FirebaseMessagingUserMetaData(in);
        }

        @Override
        public FirebaseMessagingUserMetaData[] newArray(int size) {
            return new FirebaseMessagingUserMetaData[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_storage_path() {
        return image_storage_path;
    }

    public void setImage_storage_path(String image_storage_path) {
        this.image_storage_path = image_storage_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(image_storage_path);
        parcel.writeString(name);
    }
}

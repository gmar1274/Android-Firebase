package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import app.reservation.acbasoftare.com.reservation.Interfaces.IFirebaseMessagingInbox;
import app.reservation.acbasoftare.com.reservation.Interfaces.IMessagingMetaData;

/**
 * Created by user on 2017-03-21.
 * CLASS TO REPRESENT MESSAGE VIEWS.
 * FIREBASE REPRESENTATION Of: client_messages_meta_data/client_id/{store_name:,stylist_name:,stylist_id:,stylist_photo_uri:, is_new_message_notification,store_number}
 *
 * Minimal attributes needed to message in MessageActivity...
 */
public class FirebaseInboxMetaData implements Parcelable, Comparable<FirebaseInboxMetaData>, IFirebaseMessagingInbox,IMessagingMetaData {
    private String name, id, image_storage_path;
    private boolean isRead;
    public FirebaseInboxMetaData(){

    }
    public FirebaseInboxMetaData(IMessagingMetaData user){
        this.name = user.getName();
        this.id = user.getId();
        this.image_storage_path = user.getImage_storage_path();
        this.isRead = false;
    }

   /* public FirebaseInboxMetaData(FirebaseStore store, Stylist s, CustomFBProfile prof){
        this.na
        this.stylist_photo_uri = store.getPhone()+"/images/stylists/"+s.getId();//store_phone/images/stylists/sty_id...firebase location
        this.isRead = false;
    }*/

    /**
     * DUMMY class to compare to. Main purposr to cast a child.Key from dataSnapshot convert to object.
     * @param id
     */
    public FirebaseInboxMetaData(String id){
        this.id = id;
    }
    protected FirebaseInboxMetaData(Parcel in) {
        name = in.readString();
        id = in.readString();
        image_storage_path = in.readString();
        isRead = in.readByte() != 0;
    }

    public static final Creator<FirebaseInboxMetaData> CREATOR = new Creator<FirebaseInboxMetaData>() {
        @Override
        public FirebaseInboxMetaData createFromParcel(Parcel in) {
            return new FirebaseInboxMetaData(in);
        }

        @Override
        public FirebaseInboxMetaData[] newArray(int size) {
            return new FirebaseInboxMetaData[size];
        }
    };

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    @Override
    public int hashCode(){
        return id.hashCode();
    }
    @Override
    public boolean equals(Object o){
        FirebaseInboxMetaData md = (FirebaseInboxMetaData)o;
        return this.id.equals(md.getId());
    }

    @Override
    public int compareTo(FirebaseInboxMetaData md) {
        if(this.isRead && md.isRead ==false){
            return -1;///give priority to newest message
        }
        return 0;
    }

    @Override
    public void setImage_storage_path(String url) {
        this.image_storage_path = url;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setId(String id) {
        this.id =id;
    }

    @Override
    public String getName() {
       return this.name;
    }

    @Override
    public String getImage_storage_path() {
       return this.image_storage_path;
    }

    @Override
    public String getId() {
       return this.id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeString(image_storage_path);
        parcel.writeByte((byte) (isRead ? 1 : 0));
    }
    @Override
    public String toString(){
        return Arrays.asList(this.name,this.getId(),this.image_storage_path).toString();
    }
}

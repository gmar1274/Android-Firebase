package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.Profile;

/**
 * Created by user on 2017-03-21.
 * CLASS TO REPRESENT MESSAGE VIEWS.
 * FIREBASE REPRESENTATION Of: client_messages_meta_data/client_id/{store_name:,stylist_name:,stylist_id:,stylist_photo_uri:, is_new_message_notification,store_number}
 */
public class UserMessageMetaData implements Parcelable, Comparable<UserMessageMetaData> {
    private String store_name, store_id, stylist_id, stylist_photo_uri,stylist_name, client_photo_uri, client_id;
    private boolean new_message_notification;
    public UserMessageMetaData(){

    }
    public UserMessageMetaData(FirebaseStore store, Stylist s, Profile prof){
        this.store_name = store.getName();
        this.store_id = String.valueOf(store.getStore_number());
        this.stylist_id = s.getId();
        this.stylist_name = s.getName();
        this.stylist_photo_uri = store.getPhone()+"/images/stylists/"+s.getId();//store_phone/images/stylists/sty_id...firebase location
        this.new_message_notification = false;
        this.client_photo_uri = prof.getLinkUri().toString();
        this.client_id = prof.getId();
    }

    protected UserMessageMetaData(Parcel in) {
        store_name = in.readString();
        store_id = in.readString();
        stylist_id = in.readString();
        stylist_photo_uri = in.readString();
        stylist_name = in.readString();
        new_message_notification = in.readByte() != 0;
        this.client_id = in.readString();
        this.client_photo_uri = in.readString();
    }

    public static final Creator<UserMessageMetaData> CREATOR = new Creator<UserMessageMetaData>() {
        @Override
        public UserMessageMetaData createFromParcel(Parcel in) {
            return new UserMessageMetaData(in);
        }

        @Override
        public UserMessageMetaData[] newArray(int size) {
            return new UserMessageMetaData[size];
        }
    };

    public String getClient_photo_uri() {
        return client_photo_uri;
    }

    public void setClient_photo_uri(String client_photo_uri) {
        this.client_photo_uri = client_photo_uri;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
    }

    public String getStylist_photo_uri() {
        return stylist_photo_uri;
    }

    public void setStylist_photo_uri(String stylist_photo_uri) {
        this.stylist_photo_uri = stylist_photo_uri;
    }

    public String getStylist_name() {
        return stylist_name;
    }

    public void setStylist_name(String stylist_name) {
        this.stylist_name = stylist_name;
    }

    public boolean isNew_message_notification() {
        return new_message_notification;
    }

    public void setNew_message_notification(boolean new_message_notification) {
        this.new_message_notification = new_message_notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(store_name);
        parcel.writeString(store_id);
        parcel.writeString(stylist_id);
        parcel.writeString(stylist_photo_uri);
        parcel.writeString(stylist_name);
        parcel.writeByte((byte) (new_message_notification ? 1 : 0));
        parcel.writeString(this.client_id);
        parcel.writeString(this.client_photo_uri);
    }
    @Override
    public int hashCode(){
        return stylist_id.hashCode();
    }
    @Override
    public boolean equals(Object o){
        if( !(o instanceof UserMessageMetaData))return false;
        UserMessageMetaData md = (UserMessageMetaData)o;
        return this.hashCode() == md.hashCode();
    }

    @Override
    public int compareTo(UserMessageMetaData md) {
        if(this.new_message_notification && md.new_message_notification==false){
            return -1;///give priority to newest message
        }
        return 0;
    }
}

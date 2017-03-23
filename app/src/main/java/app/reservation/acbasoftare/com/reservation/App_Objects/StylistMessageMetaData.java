package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 3/23/17.
 */

public class StylistMessageMetaData implements Comparable, Parcelable {
    private String client_name,client_id,client_photo_uri, stylist_photo_uri, stylist_id;
    private boolean new_message_notification;


    public StylistMessageMetaData(){

    }


    protected StylistMessageMetaData(Parcel in) {
        client_name = in.readString();
        client_id = in.readString();
        client_photo_uri = in.readString();
        new_message_notification = in.readByte() != 0;
        this.stylist_id = in.readString();
        this.stylist_photo_uri = in.readString();
    }

    public static final Creator<StylistMessageMetaData> CREATOR = new Creator<StylistMessageMetaData>() {
        @Override
        public StylistMessageMetaData createFromParcel(Parcel in) {
            return new StylistMessageMetaData(in);
        }

        @Override
        public StylistMessageMetaData[] newArray(int size) {
            return new StylistMessageMetaData[size];
        }
    };

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_photo_uri() {
        return client_photo_uri;
    }

    public void setClient_photo_uri(String client_photo_uri) {
        this.client_photo_uri = client_photo_uri;
    }

    public String getStylist_photo_uri() {
        return stylist_photo_uri;
    }

    public void setStylist_photo_uri(String stylist_photo_uri) {
        this.stylist_photo_uri = stylist_photo_uri;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
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
        parcel.writeString(client_name);
        parcel.writeString(client_id);
        parcel.writeString(client_photo_uri);
        parcel.writeByte((byte) (new_message_notification ? 1 : 0));
        parcel.writeString(stylist_id);
        parcel.writeString(stylist_photo_uri);
    }

    @Override
    public int compareTo(Object o) {
        StylistMessageMetaData m = (StylistMessageMetaData) o;
        if(this.new_message_notification && m.new_message_notification==false){
            return -1;
        }
        return 0;
    }
}

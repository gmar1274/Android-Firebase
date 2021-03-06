package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.Interfaces.IFirebaseMessage;

/**
 * Created by user on 2017-03-22.
 * Firebase representation of a message....used for both stylists and client communication
 */
public class FirebaseMessage implements Comparable<FirebaseMessage> , Parcelable, IFirebaseMessage{
    private String message, timestamp,sender_id; //timestamp = yyyy-MM-dd HH:mm:ss

    public FirebaseMessage(){

    }
    public FirebaseMessage(String msg , String sender_id){
        this.message = msg;
        this.sender_id = sender_id;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.timestamp = sdf.format(new Date());
    }
    protected FirebaseMessage(Parcel in) {
        message = in.readString();
        timestamp = in.readString();
        sender_id = in.readString();
    }

    public static final Creator<FirebaseMessage> CREATOR = new Creator<FirebaseMessage>() {
        @Override
        public FirebaseMessage createFromParcel(Parcel in) {
            return new FirebaseMessage(in);
        }

        @Override
        public FirebaseMessage[] newArray(int size) {
            return new FirebaseMessage[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    @Override
    public boolean equals(Object o){
        FirebaseMessage msg = (FirebaseMessage)o;
        return this.message.equals(msg.message) && this.timestamp.equals(msg.timestamp);
    }
    @Override
    public int hashCode(){
        return timestamp.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(timestamp);
        parcel.writeString(sender_id);
    }

    @Override
    public int compareTo(FirebaseMessage firebaseMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(this.timestamp);
            Date date2 = sdf.parse(firebaseMessage.timestamp);
            return  date.compareTo(date2);
        }catch (Exception e){
            e.printStackTrace();
        }
            return 0;
    }
}

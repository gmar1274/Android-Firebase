package app.reservation.acbasoftare.com.reservation.Interfaces;

/**
 * Created by user on 5/8/17.
 */

public interface IFirebaseMessage {
    String getMessage();
    String getTimestamp();
    String getSender_id();
    void setMessage(String message);
    void setTimestamp(String timestamp);
    void setSender_id(String sender_id);
}

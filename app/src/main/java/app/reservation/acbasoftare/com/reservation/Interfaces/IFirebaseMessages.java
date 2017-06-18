package app.reservation.acbasoftare.com.reservation.Interfaces;

/**
 * Created by user on 6/10/17.
 */

public interface IFirebaseMessages {
    void deleteMessage(String sender_id, String reciever_id);
    void createMessage(String sender_id, String reciever_id,String msg);
}

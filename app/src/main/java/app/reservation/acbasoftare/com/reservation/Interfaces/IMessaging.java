package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.graphics.Bitmap;

import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseMessage;

/**
 * Created by user on 5/8/17.
 */

public interface IMessaging extends IConversation {

    void sendMessage(IMessagingMetaData fromUser,FirebaseMessage message,IMessagingMetaData toSelectedUser);
    List<FirebaseMessage> loadConversation(IMessagingMetaData user, IMessagingMetaData selectedUser);
    Bitmap loadSelectedUserImage();
    Bitmap loadUserImage();
}

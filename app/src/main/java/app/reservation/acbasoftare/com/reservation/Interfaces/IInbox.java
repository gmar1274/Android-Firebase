package app.reservation.acbasoftare.com.reservation.Interfaces;

import java.util.Map;

/**
 * Created by user on 5/8/17.
 */

public interface IInbox extends IUserMessagingMetaData {
    Map<String,IMessagingMetaData> loadInbox(IMessagingMetaData user);
    void readMessage(IMessagingMetaData selectedUser);
    void deleteMessage(IMessagingMetaData selectedUser);

}

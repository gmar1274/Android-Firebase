package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.os.Parcelable;

/**
 * Created by user on 5/8/17.
 */

public interface IFirebaseMessagingInbox extends IMessagingMetaData, Parcelable {

    boolean isRead();
}

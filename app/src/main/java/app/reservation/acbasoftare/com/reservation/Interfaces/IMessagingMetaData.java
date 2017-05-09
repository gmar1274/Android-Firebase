package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.os.Parcelable;

/**
 * Created by user on 5/8/17.
 * Object that is minimum for a firebase metadata to send messages
 */

public interface IMessagingMetaData extends Parcelable{
    void setImage_storage_path(String url);
    void setName(String name);
    void setId(String id);
    String getName();
    String getImage_storage_path();
    String getId();
}

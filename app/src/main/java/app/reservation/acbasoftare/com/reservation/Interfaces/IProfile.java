package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.os.Parcelable;

/**
 * Created by user on 6/9/17.
 */

public interface IProfile {
    void setProfile(Parcelable profile_class);
    Parcelable getProfile();
    String getImagePath();
    void setImagePath(String path);
}

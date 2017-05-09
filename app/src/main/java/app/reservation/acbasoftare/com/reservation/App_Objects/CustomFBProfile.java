package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.Profile;

import app.reservation.acbasoftare.com.reservation.Utils.Utils;

/**
 * Created by user on 4/14/17.
 */

public class CustomFBProfile implements Parcelable{
    private String name,id;
    private Uri uri;
    public CustomFBProfile(Profile profile){
        this.name = profile.getName();
        this.id = profile.getId();
        this.uri = profile.getProfilePictureUri(Utils.WIDTH,Utils.HEIGHT);
    }

    protected CustomFBProfile(Parcel in) {
        name = in.readString();
        id = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<CustomFBProfile> CREATOR = new Creator<CustomFBProfile>() {
        @Override
        public CustomFBProfile createFromParcel(Parcel in) {
            return new CustomFBProfile(in);
        }

        @Override
        public CustomFBProfile[] newArray(int size) {
            return new CustomFBProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(id);
        parcel.writeParcelable(uri, i);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}

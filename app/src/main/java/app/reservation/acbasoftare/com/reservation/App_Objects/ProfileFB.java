package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.Profile;

import java.net.URI;

/**
 * Created by user on 2017-03-23.
 */
public class ProfileFB implements Parcelable {
    private Uri LinkUri;
    private String Id,Name;
    public ProfileFB(Profile prof){
       this.Id =  prof.getId();
       LinkUri = prof.getLinkUri();
        this.Name = prof.getName();
    }

    protected ProfileFB(Parcel in) {
        LinkUri = in.readParcelable(Uri.class.getClassLoader());
        Id = in.readString();
        Name = in.readString();
    }

    public static final Creator<ProfileFB> CREATOR = new Creator<ProfileFB>() {
        @Override
        public ProfileFB createFromParcel(Parcel in) {
            return new ProfileFB(in);
        }

        @Override
        public ProfileFB[] newArray(int size) {
            return new ProfileFB[size];
        }
    };

    public Uri getLinkUri() {
        return LinkUri;
    }

    public void setLinkUri(Uri linkUri) {
        LinkUri = linkUri;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(LinkUri, i);
        parcel.writeString(Id);
        parcel.writeString(Name);
    }
}

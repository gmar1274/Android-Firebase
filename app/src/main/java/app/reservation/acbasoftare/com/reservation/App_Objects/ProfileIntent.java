package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import app.reservation.acbasoftare.com.reservation.Interfaces.IProfile;

/**
 * Created by user on 6/9/17.
 */

public class ProfileIntent implements IProfile, Parcelable {

    private String image_path;
    private FirebaseStore store;
    private Stylist stylist;

    public ProfileIntent(){

    }
    public ProfileIntent(String path, FirebaseStore store){
        this.image_path = path;
        this.store = store;
    }

    public ProfileIntent(String path, Stylist sty){
        this.image_path = path;
        this.stylist = sty;
    }

    protected ProfileIntent(Parcel in) {
        image_path = in.readString();
        store = in.readParcelable(FirebaseStore.class.getClassLoader());
        stylist = in.readParcelable(Stylist.class.getClassLoader());
    }

    public static final Creator<ProfileIntent> CREATOR = new Creator<ProfileIntent>() {
        @Override
        public ProfileIntent createFromParcel(Parcel in) {
            return new ProfileIntent(in);
        }

        @Override
        public ProfileIntent[] newArray(int size) {
            return new ProfileIntent[size];
        }
    };

    @Override
    public void setProfile(Parcelable profile_class) {
        if(profile_class instanceof FirebaseStore){
            this.store = (FirebaseStore) profile_class;
        }else{
            this.stylist = (Stylist) profile_class;
        }
    }

    @Override
    public Parcelable getProfile() {
       if(store != null){
           return this.store;
       }else{
           return stylist;
       }
    }
    @Override
    public String getImagePath() {
        return this.image_path;
    }

    @Override
    public void setImagePath(String path) {
        this.image_path = path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(image_path);
        parcel.writeParcelable(store, i);
        parcel.writeParcelable(stylist, i);
    }
}

package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.GraphResponse;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by user on 4/14/17.
 */

public class CustomFBProfile implements Parcelable{
    private String name,id,email;
    private String pic_url;
    public CustomFBProfile(Profile profile){
        this.name = profile.getName();
        this.id = profile.getId();
        //this.uri = profile.getProfilePictureUri(Utils.WIDTH,Utils.HEIGHT);
        this.email = "";
        this.pic_url = profile.getProfilePictureUri(50,50).getPath();
    }
    public CustomFBProfile(Profile profile, HashMap<String,String> extra){
        this.name = profile.getName();
        this.id = profile.getId();
        //this.uri = profile.getProfilePictureUri(Utils.WIDTH,Utils.HEIGHT);
        this.email = extra.get("email");
        this.pic_url = null;
    }

    /**
     *
     * @param response FB API graph call
     */
    public CustomFBProfile(GraphResponse response) {
        JSONObject obj = response.getJSONObject();
        try {
            this.id = obj.getString("id");
            this.name = obj.getString("name");
            this.email = obj.getString("email");
            this.pic_url = obj.getJSONObject("picture").getJSONObject("data").getString("url");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected CustomFBProfile(Parcel in) {
        name = in.readString();
        id = in.readString();
        //uri = in.readParcelable(Uri.class.getClassLoader());
        this.email = in.readString();
        this.pic_url = in.readString();
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
        parcel.writeString(email);
        parcel.writeString(pic_url);
       // parcel.writeParcelable(uri, i);
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

   /* public Uri getUri() {
        return uri;
    }*/

   /* public void setUri(Uri uri) {
        this.uri = uri;
    }*/

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}


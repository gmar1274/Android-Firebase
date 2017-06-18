package app.reservation.acbasoftare.com.reservation.Interfaces;

import java.util.HashMap;

import app.reservation.acbasoftare.com.reservation.App_Objects.CustomFBProfile;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;

/**
 * Created by user on 6/13/17.
 */

public interface IBio {
    void call(String numberTocall);
    void startMessagingActivity(CustomFBProfile prof, FirebaseStore store, Stylist s,HashMap<String,String> stylist_bitmaps);
}

package app.reservation.acbasoftare.com.reservation.Interfaces;

import android.widget.ImageView;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;

/**
 * Created by user on 6/9/17.
 */

public interface IFirebaseStylistAsyncCall {
    //void queryStylists(IOnCallback callback);
    //void callback(Stylist stylist);
    void queryStylist(FirebaseStore store, ImageView iv, Stylist stylist);
    Stylist OnCallback(Stylist sty);
}

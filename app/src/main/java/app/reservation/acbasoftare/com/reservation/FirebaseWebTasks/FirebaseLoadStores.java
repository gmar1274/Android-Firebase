package app.reservation.acbasoftare.com.reservation.FirebaseWebTasks;

import android.app.Activity;

import java.util.ArrayList;

import app.reservation.acbasoftare.com.reservation.App_Objects.Store;

/**
 * Created by user on 1/28/17.
 */

public class FirebaseLoadStores {
    private ArrayList<Store> list;
    private Activity activity;
    public FirebaseLoadStores(Activity act, ArrayList<Store> list){
        this.list = list;
        this.activity = act;
    }
    public FirebaseLoadStores(){}

}

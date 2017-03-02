package app.reservation.acbasoftare.com.reservation.App_Objects;

import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;

/**
 * Created by user on 2016-12-10.
 */
public class MyIntent {
    public Store store;
    public Stylist stylist;
    public  SalonService salonService;
    public Date date;
    public int selectedPosition;

    public MyIntent(Date date, Store s , Stylist ss , SalonService sss, int selectedPos){
        this.store=s;
        this.stylist=ss;
        this.salonService=sss;
        this.date=date;
        this.selectedPosition = selectedPos;
    }
    public void reset(){
        stylist=null;
        store=null;
        salonService=null;
        this.date=null;
        this.selectedPosition = 0;
    }
}
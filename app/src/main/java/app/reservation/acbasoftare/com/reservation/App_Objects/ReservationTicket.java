package app.reservation.acbasoftare.com.reservation.App_Objects;

/**
 * Created by user on 12/12/16.
 * Wanted to use this as a invoice thing...
 */
public class ReservationTicket {
    public Customer customer;
    public Stylist stylist;
    public SalonService salonService;
    public TimeSet time;
    public ReservationTicket(Stylist stylist,SalonService ss,Customer c, TimeSet time){
        this.stylist=stylist;
        this.salonService=ss;
        this.customer=c;
        this.time=time;
    }
    public  ReservationTicket(){

    }
}

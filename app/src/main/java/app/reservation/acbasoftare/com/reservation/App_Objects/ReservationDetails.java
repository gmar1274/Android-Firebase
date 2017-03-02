package app.reservation.acbasoftare.com.reservation.App_Objects;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by user on 12/14/16.
 */
public class ReservationDetails {
    public Date datetime;
    public  String service_name,notes;
    public Customer customer;
    public  TimeSet timeSet;
    public ReservationDetails(TimeSet ts, String service_name , Customer cust, String notes){
        this.service_name=service_name;
        this.notes=notes;
        this.customer = cust;
        this.timeSet = ts;
        this.datetime=ts.getLowerBound();
    }
    public String getAppointmentTime(){
        return timeSet.getTimeRangeFormat();
    }
    public String toString(){
        return "{RD:"+this.timeSet+"}";
    }

}

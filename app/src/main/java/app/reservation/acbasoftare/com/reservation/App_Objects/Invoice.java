package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2/26/17.
 *
 * tHIS class needs to be indexed by the DAY.. so the key will be DAY and info will be price, time.
 * TIME in military, total_Sale is before STRIPES 2.9% plus 30cents. So (SUM of all total_p)*.029 + (# of transactions * .3)
 *
 */

public class Invoice implements Parcelable, Comparable<Invoice> {
    private double total_sale;
    private String time, stylist_id;
    private long unique_ticket;
    public Invoice(){

    }
    public Invoice(Ticket t, double ticket_price){
        this.total_sale = ticket_price;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
       // this.day = sdf.format(date);
        this.time = time.format(date);
        this.stylist_id = t.stylist_id;
        this.unique_ticket = t.unique_id;
    }

    public Invoice(Parcel in) {
        total_sale = in.readDouble();
        time = in.readString();
        stylist_id = in.readString();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(total_sale);
        parcel.writeString(time);
        parcel.writeString(stylist_id);
    }

    public double getTotal_sale() {
        return total_sale;
    }

    public void setTotal_sale(double total_sale) {
        this.total_sale = total_sale;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public void setStylist_id(String stylist_id) {
        this.stylist_id = stylist_id;
    }

    @Override
    public int compareTo(Invoice invoice) {
        if(this.unique_ticket < invoice.unique_ticket){
            return -1;
        }else if(this.unique_ticket > invoice.unique_ticket){
            return 1;
        }else
        {
            return 0;
        }
    }
}

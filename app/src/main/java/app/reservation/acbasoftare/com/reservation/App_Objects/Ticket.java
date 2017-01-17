package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by user on 2016-11-26.
 */
public class Ticket implements Parcelable {
    public String name;
    public String phone;
    public String ticket;
    public String stylist;
    public String store_id;
    public Ticket(){}

    public Ticket(String store_id,String ticket,String name,String stylist,String phone){
        this.ticket=ticket;
        this.name=name;
        this.store_id=store_id;
        this.phone=phone;
        this.stylist=stylist;
    }

    protected Ticket(Parcel in) {
        name = in.readString();
        phone = in.readString();
        ticket = in.readString();
        stylist = in.readString();
        store_id = in.readString();
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
         parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(store_id);
        parcel.writeString(ticket);
        parcel.writeString(stylist);
    }
}

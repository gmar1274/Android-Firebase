package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by user on 2016-11-26.
 */
public class Ticket implements Parcelable {
    public String name;
    public String phone;
    public String ticket;//the relative ticket for the stylist
    public String stylist,stylist_id;
    public String store_id;

    public long unique_id; //alias for the current ticket

    public Ticket(){}
@Override
public boolean equals(Object obj){//same unique(current store) number, same ticket in line for a stylist, same stylist
    Ticket t = (Ticket) obj;
    return t.unique_id==this.unique_id && t.ticket.equalsIgnoreCase(ticket) && stylist.equalsIgnoreCase(stylist);
}
public String toString(){
    return name+ String.format("%"+ 20 +"s", " ")+unique_id+ String.format("%"+ 5 +"s", " ")+stylist+ String.format("%"+ 5 +"s", " ") + " ??? ";
}
    public Ticket(long unique_id,String ticket,String name,String sty_id,String stylist,String phone){
        this.ticket=ticket;
        this.name=name;
        this.phone=phone;
        this.stylist=stylist;
        this.stylist_id = sty_id;
        this.unique_id = unique_id;
    }

    protected Ticket(Parcel in) {
        name = in.readString();
        phone = in.readString();
        ticket = in.readString();
        stylist = in.readString();
        store_id = in.readString();
        unique_id = in.readLong();
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
        parcel.writeLong(unique_id);
    }
//FIREBASE GETTERS


    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getTicket() {
        return ticket;
    }

    public String getStylist() {
        return stylist;
    }

    public String getStylist_id() {
        return stylist_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public long getUnique_id() {
        return unique_id;
    }//END FIREBASE GETTERS...
}


package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016-11-26.
 */
public class Ticket implements Parcelable, Comparable<Ticket> {
    public String name;
    public String phone;
    public String ticket_number;//the relative ticket_number for the stylist
    public String stylist,stylist_id;
    public String store_id;
    public String readyBy;
    public long unique_id; //alias for the current ticket_number or absolute number
    public String timestamp;//time when
    public Ticket(){}

    protected Ticket(Parcel in) {
        name = in.readString();
        phone = in.readString();
        ticket_number = in.readString();
        stylist = in.readString();
        stylist_id = in.readString();
        store_id = in.readString();
        readyBy = in.readString();
        unique_id = in.readLong();
        timestamp = in.readString();
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

    public String getReadyBy() {
        return readyBy;
    }

    public void setReadyBy(String readyBy) {
        this.readyBy = readyBy;
    }

    @Override
public boolean equals(Object obj){//same unique(current store) number, same ticket_number in line for a stylist, same stylist
    Ticket t = (Ticket) obj;
    return t.unique_id==this.unique_id ;//&& t.ticket_number.equalsIgnoreCase(ticket_number) && stylist.equalsIgnoreCase(stylist);
}
public String toString(){
    return "UNQ: "+unique_id+" NAME: "+name+" Phone: "+phone;//name+ String.format("%"+ 20 +"s", " ")+unique_id+ String.format("%"+ 5 +"s", " ")+stylist+ String.format("%"+ 5 +"s", " ") + " ??? ";
}
    public Ticket(long unique_id, String ticket_number, String name, String sty_id, String stylist, String phone){
        this.ticket_number = ticket_number;
        this.name=name;
        this.phone=phone;
        this.stylist=stylist;
        this.stylist_id = sty_id;
        this.unique_id = unique_id;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * We use this constructor to simulate the user next ticket
     * @param latestTicket
     */
    public Ticket(Ticket latestTicket){
        this.unique_id = latestTicket.unique_id + 1;
    }


//FIREBASE GETTERS

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getTicket_number() {
        return ticket_number;
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

    @Override
    public int compareTo(Ticket ticket) {
        if(this.unique_id < ticket.unique_id)return -1;
        else  if (this.unique_id > ticket.unique_id) return 1;
        else return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(phone);
        parcel.writeString(ticket_number);
        parcel.writeString(stylist);
        parcel.writeString(stylist_id);
        parcel.writeString(store_id);
        parcel.writeString(readyBy);
        parcel.writeLong(unique_id);
        parcel.writeString(timestamp);
    }
}


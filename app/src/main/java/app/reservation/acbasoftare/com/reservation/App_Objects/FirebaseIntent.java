package app.reservation.acbasoftare.com.reservation.App_Objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by user on 1/26/17.
 */

public class FirebaseIntent implements Parcelable {
    private ArrayList<Ticket> tickets;
    private ArrayList<Stylist> stylist_list;
    private int stylist_position;
    public FirebaseIntent(){
    }
    public FirebaseIntent(ArrayList<Stylist>s, ArrayList<Ticket>t, int sty_pos){
        this.tickets=t;
        this.stylist_list=s;

        this.stylist_position=sty_pos;
    }

    protected FirebaseIntent(Parcel in) {
        tickets = in.createTypedArrayList(Ticket.CREATOR);
        stylist_list = in.createTypedArrayList(Stylist.CREATOR);
        stylist_position = in.readInt();
       // store = in.readParcelable(Store.class.getClassLoader());

    }

    public static final Creator<FirebaseIntent> CREATOR = new Creator<FirebaseIntent>() {
        @Override
        public FirebaseIntent createFromParcel(Parcel in) {
            return new FirebaseIntent(in);
        }

        @Override
        public FirebaseIntent[] newArray(int size) {
            return new FirebaseIntent[size];
        }
    };

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public ArrayList<Stylist> getStylist_list() {
        return stylist_list;
    }

    public int getStylist_position() {
        return stylist_position;
    }




    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(tickets);
        parcel.writeTypedList(stylist_list);
        parcel.writeInt(stylist_position);
      ///  parcel.writeParcelable(store, i);
    }
}

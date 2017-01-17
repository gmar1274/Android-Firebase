package app.reservation.acbasoftare.com.reservation.Fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.a;
import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.rootView_Reservation;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ReservationFragment extends Fragment {
    private Date date;
    private Stylist stylist;
    private Store store;
    private SalonService service;

    public ReservationFragment() {

    }

    public void init(Date d, Stylist s, Store store, SalonService ss) {
        // Required empty public constructor
        this.date=d;
        this.store=store;
        this.stylist=s;
        this.service=ss;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ReservationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationFragment newInstance(String param1) {
        ReservationFragment fragment=new ReservationFragment();
        Bundle args=new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        a.getWindow().setStatusBarColor(a.getResources().getColor(R.color.colorPrimaryDark));
        if(getArguments() != null) {
        } else {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_reservation, container, false);
        Button back=(Button) view.findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void delete() {
        Log.d("In Method:", "Btn pressed");
      MainActivity.a.getFragmentManager().beginTransaction().remove(this).commit();
        ((ViewGroup)MainActivity.rootView_Reservation.getParent()).removeView(rootView_Reservation);
        MainActivity.tabLayout.addView(rootView_Reservation,2);


    }
}
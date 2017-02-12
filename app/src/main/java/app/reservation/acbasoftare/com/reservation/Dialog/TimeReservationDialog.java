package app.reservation.acbasoftare.com.reservation.Dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.R;

/**
 * Created by user on 12/8/16.
 */
public class TimeReservationDialog {
        private boolean success = false;
        private AlertDialog.Builder alert;
        private AlertDialog alertd;
        private Store store;
        private Stylist stylist;
        private MainActivity ma;
        public boolean isSuccess() {
            return this.success;
        }

        public void showReservationGUI(MainActivity ma) {
            this.ma = ma;
            alert = new AlertDialog.Builder(ma);
            alert.setTitle("Reservation Dialog");
            LayoutInflater inflater = ma.getLayoutInflater();
            final View layout = inflater.inflate(R.layout.activity_reservation, null);
            alert.setView(layout);
            alert.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });
            alertd = alert.create();
            alertd.setCancelable(false);
            alertd.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    // if (success == false) {
                    Button button = alertd.getButton(DialogInterface.BUTTON_POSITIVE);////////on yes or ok
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            execute();

                        }
                    });
                }
            });
            alertd.show();
        }
        /**
         * Perform the db reservation
         * */
        private void execute(String... params) {

        }

        private void displayError() {
            success = false;
            displayMessage("Please fill in all fields.");
        }

        private void displayMessage(final String msg) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ma.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                            if (success) alertd.dismiss();
                        }
                    }
            );
        }




}

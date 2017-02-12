package app.reservation.acbasoftare.com.reservation.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;
import app.reservation.acbasoftare.com.reservation.App_Activity.ReservationActivity;
import app.reservation.acbasoftare.com.reservation.App_Objects.ACBAPackage;
import app.reservation.acbasoftare.com.reservation.App_Objects.Encryption;
import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.ParamPair;
import app.reservation.acbasoftare.com.reservation.App_Objects.SalonService;
import app.reservation.acbasoftare.com.reservation.App_Objects.Store;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;
import app.reservation.acbasoftare.com.reservation.R;
import app.reservation.acbasoftare.com.reservation.WebTasks.LockDBPreserveSpot;
import app.reservation.acbasoftare.com.reservation.WebTasks.WebService;

import static java.lang.Integer.parseInt;

/**
 * Created by user on 2016-08-09.
 */
public class CreditCardDialog {
    private WebService ws;
    private final String pk = "pk_test_txeeWRDZ5WJKfYiMa9ItQT9I";
    public String phone;//user device phone
    private TimeSet appointment;
    private SalonService salon_service;
    private int store_pos, stylist_pos;
    private String card_number, ccv, name;
    private int expr_month, expr_year, amount;
    private boolean success = false;
    private AlertDialog.Builder alert;
    private AlertDialog alertd;
    private FirebaseStore store;
    private Stylist stylist;
    private View rootView;
    private ReservationActivity activity;
    private Card card;
    private MainActivity act;
    public CreditCardDialog(MainActivity ma,int store_pos, int styl_pos, String phone) {
        this.store_pos = store_pos;
        this.stylist_pos = styl_pos;
        this.store = ma.store_list.get(store_pos);
        this.stylist = ma.stylists_list.get(styl_pos);
        this.phone = phone;
        this.activity=null;
        this.ws = new WebService();
        this.act = ma;
    }

    public CreditCardDialog(ReservationActivity c, FirebaseStore store, Stylist stylist, SalonService ss, TimeSet datetime) {///month reservation
        this.activity = c;
        this.store = store;
        this.stylist = stylist;
        this.salon_service = ss;
        this.appointment = datetime;
    }
    public CreditCardDialog(Activity act, WebService ws){
        try {
            this.act = (MainActivity) act;
        }catch (Exception e){
            e.printStackTrace();
        }
        this.store = new FirebaseStore();
        this.stylist = new Stylist("1212",true);
        this.phone = "";//user phone
        this.ws = ws;
    }
    public CreditCardDialog(Activity act){

        try {
            this.act = (MainActivity) act;
        }catch (Exception e){
            e.printStackTrace();
        }
        this.store = new FirebaseStore();
        this.stylist = new Stylist("1212",true);
        this.phone = "";//user phone

    }
    public boolean isSuccess() {
        return this.success;
    }

    /**
     * This is for the non live day. For future reference.
     */
    public void showCreditCardDialogForReservationCalendar() {

        alert = new AlertDialog.Builder(activity);

        alert.setTitle("Enter Contact Information");

        LayoutInflater inflater = activity.getLayoutInflater();

        final View layout = inflater.inflate(R.layout.make_reservation_fragment, null);
        EditText phone_et = (EditText) layout.findViewById(R.id.reservation_phone);
        phone_et.setText(phone);
        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
        DecimalFormat df = new DecimalFormat("$0.00");
        amount.setText(df.format(store.getReservation_calendar_price()));
        TextView store_tv = (TextView) layout.findViewById(R.id.store_reservation_textview);
        store_tv.setText("Store: " + store.getName().toUpperCase() + "\nService: " + salon_service.getName().toUpperCase());
        TextView stylist_tv = (TextView) layout.findViewById(R.id.stylist_reservation_textview);
        stylist_tv.setText("Stylist: " + stylist.getName().toUpperCase() + "\nDate: " + appointment.getDateFormat() + "\nTime: " + appointment.getTimeRangeFormat());
        alert.setView(layout);

        alert.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //on reserv
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //cancel
            }
        });
        alertd = alert.create();
        alertd.setCancelable(false);
        alertd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                // if (success == false) {

                Button button = alertd.getButton(DialogInterface.BUTTON_POSITIVE);////////on yes or ok
                button.setOnClickListener(new View.OnClickListener() {///on OK listener
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) layout.findViewById(R.id.reservation_name);
                        EditText ccv = (EditText) layout.findViewById(R.id.reservation_ccv);
                        EditText creditcard = (EditText) layout.findViewById(R.id.reservation_creditcard);
                        EditText exp_month = (EditText) layout.findViewById(R.id.reservation_expmonth);
                        EditText exp_yr = (EditText) layout.findViewById(R.id.reservation_expyear);
                        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
                        String amount_billed = amount.getText().subSequence(1, amount.getText().toString().length()).toString();
                        amount_billed = amount_billed.replace("$", "");//$1.00->1.00
                        int amount_b = new Double(Double.parseDouble(amount_billed) * 100).intValue();
                        String yr = exp_yr.getText().toString();
                        if (yr.length() == 2) {
                            Date d = new Date();
                            int yrr = d.getYear() + 1900;
                            yr = "" + ((yrr / 100) + parseInt(yr));//////////////////this could be huge error
                        }
                        EditText phone_et = (EditText) layout.findViewById(R.id.reservation_phone);
                        phone = phone_et.getText().toString();
                        if(phone ==null || phone.length()==0){
                            displayError();
                            return;
                        }
                        execute(creditcard.getText().toString(), ccv.getText().toString(), exp_month.getText().toString(), yr, name.getText().toString(), "" + amount_b);

                    }
                });
            }


        });
        alertd.show();


    }

    public void showCreditCardDialog() {
        alert = new AlertDialog.Builder(this.act);

        alert.setTitle("Enter Contact Information");

        LayoutInflater inflater = this.act.getLayoutInflater();

        final View layout = inflater.inflate(R.layout.make_reservation_fragment, null);

        EditText phone_et = (EditText) layout.findViewById(R.id.reservation_phone);
        phone_et.setText(phone);
        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
        DecimalFormat df = new DecimalFormat("$0.00");
        amount.setText(df.format(store.getTicket_price()));
        TextView store_tv = (TextView) layout.findViewById(R.id.store_reservation_textview);
        store_tv.setText("Store: " + store.getName().toUpperCase());
        TextView stylist_tv = (TextView) layout.findViewById(R.id.stylist_reservation_textview);
        stylist_tv.setText("Stylist: " + stylist.getName().toUpperCase());

        alert.setView(layout);

        alert.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               // LockDBPreserveSpot db = new LockDBPreserveSpot(act);
                // db.execute(store.getPhone(), "" + MainActivity.ticket_number);// Canceled.
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
                        EditText name = (EditText) layout.findViewById(R.id.reservation_name);
                        EditText ccv = (EditText) layout.findViewById(R.id.reservation_ccv);
                        EditText creditcard = (EditText) layout.findViewById(R.id.reservation_creditcard);
                        EditText exp_month = (EditText) layout.findViewById(R.id.reservation_expmonth);
                        EditText exp_yr = (EditText) layout.findViewById(R.id.reservation_expyear);
                        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
                        String amount_billed = amount.getText().subSequence(1, amount.getText().toString().length()).toString();
                        amount_billed = amount_billed.replace("$", "");//$1.00->1.00
                        int amount_b = new Double(Double.parseDouble(amount_billed) * 100).intValue();
                        String yr = exp_yr.getText().toString();
                        if (yr.length() == 2) {
                            Date d = new Date();
                            int yrr = d.getYear() + 1900;
                            yr = "" + ((yrr / 100) + parseInt(yr));//////////////////this could be huge error
                        }
                        execute(creditcard.getText().toString(), ccv.getText().toString(), exp_month.getText().toString(), yr, name.getText().toString(), "" + amount_b);

                    }
                });
            }


        });
        alertd.show();


    }
    /**
     * TEST DEBUGGG CREDIT CARD DIALOG
     */
    public void showCreditCardDialog(boolean test) {
        alert = new AlertDialog.Builder(act);//

        alert.setTitle("Enter Contact Information");

        LayoutInflater inflater = act.getLayoutInflater();

        final View layout = inflater.inflate(R.layout.make_reservation_fragment, null);

        EditText phone_et = (EditText) layout.findViewById(R.id.reservation_phone);
        phone_et.setText(phone);
        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
        DecimalFormat df = new DecimalFormat("$0.00");
        amount.setText(df.format(store.getTicket_price()));
        TextView store_tv = (TextView) layout.findViewById(R.id.store_reservation_textview);
        store_tv.setText("Store: " + store.getName().toUpperCase());
        TextView stylist_tv = (TextView) layout.findViewById(R.id.stylist_reservation_textview);
        stylist_tv.setText("Stylist: " + stylist.getName().toUpperCase());

        alert.setView(layout);

        alert.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // LockDBPreserveSpot db = new LockDBPreserveSpot();
                //db.execute(store.getPhone(), "" + MainActivity.ticket_number);// Canceled.
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
                        EditText name = (EditText) layout.findViewById(R.id.reservation_name);
                        EditText ccv = (EditText) layout.findViewById(R.id.reservation_ccv);
                        EditText creditcard = (EditText) layout.findViewById(R.id.reservation_creditcard);
                        EditText exp_month = (EditText) layout.findViewById(R.id.reservation_expmonth);
                        EditText exp_yr = (EditText) layout.findViewById(R.id.reservation_expyear);
                        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
                        String amount_billed = amount.getText().subSequence(1, amount.getText().toString().length()).toString();
                        amount_billed = amount_billed.replace("$", "");//$1.00->1.00
                        int amount_b = new Double(Double.parseDouble(amount_billed) * 100).intValue();
                        String yr = exp_yr.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String year = sdf.format(new Date());
                        yr = year.substring(0,2) + yr;
                        execute(creditcard.getText().toString(), ccv.getText().toString(), exp_month.getText().toString(), yr, name.getText().toString(), "" + amount_b);

                    }
                });
            }


        });
        alertd.show();


    }


    /**
     * Perform the credit card authentication to stripe.
     * Flow is coming the credit card fields..
     */
    private void execute(String... params) {
        try {
            for (int i = 0; i < params.length; ++i) {
                if (params[i] == null || params[i].length() == 0) {
                    displayError();
                    return;
                }
            }
            card_number = params[0];
            ccv = params[1];
            expr_month = parseInt(params[2]);
            expr_year = parseInt(params[3]);
            name = params[4];
            amount = parseInt(params[5]);
            if(activity!=null){
                activity.getCustomer().setName(name);
            }

        } catch (Exception e) {
            e.printStackTrace();
            displayError();
            return;
        }
        card = new Card(card_number, expr_month, expr_year, ccv);
        if (!card.validateCard() || !card.validateCVC() || !card.validateExpiryDate() || !card.validateExpYear() || !card.validateExpMonth()) {
            error("Credit card is invalid.");
            return;

        }
        ////////////////////////////then i think we can close the dialog
        alertd.dismiss();////////////////////not sure here
        final ProgressDialog pd = ProgressDialog.show(act,"Grabbing Ticket","Please Wait...",true,false);
        pd.show();

        Stripe stripe = null;

        try {
            stripe = new Stripe(pk);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        // com.stripe.Stripe.apiKey = sk;
        if (stripe != null) {
            stripe.createToken(card, new TokenCallback() {
                        @Override
                        public void onError(Exception error) {
                            error("Network Error. Please be patience while the problem is being fixed.");
                        }

                        public void onSuccess(final Token token) {
                            Thread t = new Thread() {
                                public void run() {

                                    webCallMakeCharge(token,pd);//makeCharge(token);
                                }
                            };
                            t.start();
                        }
                    }
            );
        }
    }
    private void webCallMakeCharge(Token token,ProgressDialog pd){
        if(ws != null) {
            ArrayList<ParamPair> l=new ArrayList<>();
            l.add(new ParamPair("stripeToken", token.getId()));
            l.add(new ParamPair("credit_code", Encryption.encryptPassword("acbacreditacba")));
            l.add(new ParamPair("amount", amount));
            l.add(new ParamPair("name", name));
            l.add(new ParamPair("email", "gnmartinezedu@hotmail.com"));
            l.add(new ParamPair("fingerprint", token.getCard().getFingerprint()));
            JSONObject ob=ws.makeHttpRequest(WebService.createChargeURL, l);
            if(ob != null) {//payment successful
                firebaseAddTicket(store, stylist, name, phone,pd);
                //Log.e("IN CREDIT:: ", ob.toString());
            } else {
                Log.e("IN CREDIT:: ", "ERROR in creating params");
                Toast.makeText(this.act,"Payment unsuccessful. No charges were made. Try again.",Toast.LENGTH_LONG).show();
            }
            alertd.dismiss();
        }
    }

    /**
     * Calls firebase to add ticket
     */
    private void firebaseAddTicket(FirebaseStore s, Stylist sty, String cust_name,String phone,ProgressDialog pd) {
        act.sendTicket(s,sty,cust_name,phone,pd);
    }

    private void displayError() {
        success = false;
        error("Please fill in all fields.");
    }

    private void error(final String msg) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        //DEBUG
                        if(act != null){
                            Toast.makeText(act.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                            return;
                        }
                        //DEBUGGG
                        if (activity != null) {
                            Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(act.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                        if (success) alertd.dismiss();
                    }
                }
        );
    }
    /**
     *
     *
     * NEW MEMBERSHIP
     *
     *
     *
     */
    public void newMemberShipShowCreditCardDialog(Activity a, ACBAPackage plan) {
        if(plan == null){
            plan = new ACBAPackage();
            plan.debug();//
        }
        //this.act = a;
        alert = new AlertDialog.Builder(act);//

        alert.setTitle("Billing Information");

        LayoutInflater inflater = act.getLayoutInflater();

        final View layout = inflater.inflate(R.layout.make_reservation_fragment, null);

        EditText phone_et = (EditText) layout.findViewById(R.id.reservation_phone);
        //phone_et.setText(phone);
        phone_et.setVisibility(View.GONE);
        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
        DecimalFormat df = new DecimalFormat("$0.00");


        amount.setText(df.format(plan.getPrice().doubleValue()));
        TextView store_tv = (TextView) layout.findViewById(R.id.store_reservation_textview);
        //store_tv.setText("Store: " + store.getName().toUpperCase());
        store_tv.setVisibility(View.GONE);
        TextView stylist_tv = (TextView) layout.findViewById(R.id.stylist_reservation_textview);
        // stylist_tv.setText("Stylist: " + stylist.getName().toUpperCase());
        stylist_tv.setVisibility(View.GONE);

        alert.setView(layout);

        alert.setPositiveButton("Reserve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //
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
                        EditText name = (EditText) layout.findViewById(R.id.reservation_name);
                        EditText ccv = (EditText) layout.findViewById(R.id.reservation_ccv);
                        EditText creditcard = (EditText) layout.findViewById(R.id.reservation_creditcard);
                        EditText exp_month = (EditText) layout.findViewById(R.id.reservation_expmonth);
                        EditText exp_yr = (EditText) layout.findViewById(R.id.reservation_expyear);
                        TextView amount = (TextView) layout.findViewById(R.id.textview_reservation_amount);
                        String amount_billed = amount.getText().subSequence(1, amount.getText().toString().length()).toString();
                        amount_billed = amount_billed.replace("$", "");//$1.00->1.00
                        int amount_b = new Double(Double.parseDouble(amount_billed) * 100).intValue();
                        String yr = exp_yr.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                        String year = sdf.format(new Date());
                        yr = year.substring(0,2) + yr;


                        execute(creditcard.getText().toString(), ccv.getText().toString(), exp_month.getText().toString(), yr, name.getText().toString(), "" + amount_b);

                    }
                });
            }


        });
        alertd.show();


    }

    /////////////////////////////end new membership
    /**
     * API call to STRIPE
     private void makeCharge(final Token token) {
     // Thread t = new Thread() {
     // public void run() {
     Customer c = new Customer();
     //c.setDefaultCard(token.getCard().toString());
     //c.setDescription("Remember Name:[" + token.getCard().getName() + "]");
     Map<String, Object> chargeParams = new HashMap<String, Object>();
     chargeParams.put("amount", amount); // amount in cents, again
     chargeParams.put("currency", "usd");
     chargeParams.put("source", token.getId());//chargeParams.put("source", "tok_8yOnTLA0hnB9Sh");//chargeParams.put("source", t.toString());
     chargeParams.put("description", "Haircut/Salon ACBA Reservation Mobile App");
     /* try {
     // Charge charge = Charge.create(chargeParams);
     success = true;
     if(activity!=null){///then reservation activity
     //charge went through
     // ReservationWebTask rwt = new ReservationWebTask(activity,card,charge,phone);///attempt to make reservation
     // rwt.execute();
     this.alertd.dismiss();
     }else {
     LockDBPreserveSpot db = new LockDBPreserveSpot();
     MainActivity.isSuccess = true;
     db.execute(store.getPhone(), MainActivity.ticket_number + "", name, stylist.getID());//update
     error("Success!");
     }
     } catch (AuthenticationException e) {
     e.printStackTrace();
     } catch (InvalidRequestException e) {
     e.printStackTrace();
     } catch (APIConnectionException e) {
     e.printStackTrace();
     } catch (CardException e) {
     e.printStackTrace();
     } catch (APIException e) {
     e.printStackTrace();
     }*/
    //}
}
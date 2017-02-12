package app.reservation.acbasoftare.com.reservation.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import app.reservation.acbasoftare.com.reservation.App_Objects.FirebaseStore;
import app.reservation.acbasoftare.com.reservation.App_Objects.Stylist;
import app.reservation.acbasoftare.com.reservation.App_Objects.TimeSet;

/**
 * Created by user on 12/6/16.
 */
public class Utils {
    /*
    * Needs db analytics to gather intellignet wait per store...
    * Not in commission at this moment...
    * */

    public static String calculateWait(int waiting, double wait_weight_in_min) {
        double wait = waiting*wait_weight_in_min;
        int hour= new Integer(""+wait/60);
        int min= new Integer(""+wait%60);
        if(hour>0)return hour+" hrs "+min+" mins";
        return min+" mins";
    }

    /*Should return the new time in advanced.
    * */
    public static String calculateWait(int waiting) {
        int wait = 45 * waiting;//45 mins * #wait
        int hour= new Integer(""+wait/60);
        int min= new Integer(""+wait%60);
        if(hour>0)return hour+" hrs "+min+" mins";
        return min+" mins";
    }
    public static String calculateTimeReady(int waiting,double stylist_average_wait){
        int wait = Utils.minTomilliseconds(new Integer(""+stylist_average_wait).intValue()) * waiting;//45 mins to milliseconds
        Date clear = new Date();
        Date wait_date = new Date(Math.abs(wait + clear.getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");//hour:min format
        return sdf.format(wait_date);
    }
    public static String calculateTimeReady(int waiting){
        int wait = Utils.minTomilliseconds(45) * waiting;//45 mins to milliseconds
        Date clear = new Date();
        Date wait_date = new Date(Math.abs(wait + clear.getTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");//hour:min format
        return sdf.format(wait_date);
    }

    /**
     * Returns mins to milliseconds
     */
    public static int minTomilliseconds(int i) {
        return i * 60 * 1000;
    }

    /**
     * public static Bitmap resize(Bitmap image,int width,int height) {
     * Bitmap b = image;
     * Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, true);// filter attribute set to true
     * return bitmapResized;
     * }
     */
    public static ArrayList<Stylist> getArrayListStylist(ArrayList<Stylist> list) {
        ArrayList<Stylist> sty = new ArrayList<>();
        sty.addAll(list);
        sty.remove(0);
        return sty;
    }

    public static String getTimeRange(Date start_datetime, long service_seconds) {
        Log.d("timeeeee: ", "" + service_seconds);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm a");
        Date now = new Date(start_datetime.getTime() + service_seconds);
        return sdf.format(start_datetime) + " - " + sdf.format(now);
    }

    public static String getTimeRange(Date start_datetime, Date end) {
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm a");
        return sdf.format(start_datetime) + " - " + sdf.format(end);
    }

    public static long hourToseconds(int hour) {
        return hour * 60 * 60;
    }

    public static long minToseconds(int min) {
        return min * 60;
    }

    public static Drawable resize(Context c, Drawable image, int w, int h) {
        Bitmap b = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapResized = resize(b, w, h);
        return new BitmapDrawable(c.getResources(), bitmapResized);
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static boolean isSameDay(Date today, Date start) {
        return today.getDate() == start.getDate() && today.getMonth() == start.getMonth() && today.getYear() == start.getYear();
    }

    public static Date getDateFromServiceAdded(Date start, long seconds) {
        return new Date(start.getTime() + seconds);
    }

    /***
     * @param today        - A date object
     * @param openingHour- must be in the format of a mysql TIME. ex) 05:00:00
     * @return a new Date object initialized to param openingHour
     */
    public static Date getDateFromAGivenTime(Date today, String openingHour) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat c = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(c.format(today) + " " + openingHour);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * VERY IMPORTANT: algorithm to determine the most efficient time of the specified SERVICE(given in milliseconds)
     *
     * @param open_hour    lower bound
     * @param closing_hour upperbound
     * @param milliseconds delta time
     */
    public static ArrayList<String> getAvailableTimes(final Date open_hour, final Date closing_hour, ArrayList<ArrayList<Date>> taken_list, long milliseconds) {
        ArrayList<String> formatted = new ArrayList<>();
        Date pointer = open_hour;
        while (pointer.compareTo(closing_hour) <= 0) { //same or less than closing
            TimeSet ts = new TimeSet(pointer, milliseconds);//get TimeSet of current [lb,lower_bound+service]
            if (!taken_list.isEmpty()) {//trying to squeeze as much room as possible
                TimeSet taken_ts = new TimeSet(taken_list.get(0).get(0), taken_list.get(0).get(1));
                if (ts.isLowerBoundDisjoint(taken_ts) && ts.isSubSet(open_hour, closing_hour)) {
                    formatted.add(ts.get12HourFormat());
                    pointer = ts.getUpperBound();
                } else {//not leftDisjoint and is taken so get takens upper bound and move to next in taken
                    pointer = taken_ts.getUpperBound();
                    taken_list.remove(0);
                }
            } else if (ts.isSubSet(open_hour, closing_hour)) {
                formatted.add(ts.get12HourFormat());
                pointer = ts.getUpperBound();
            } else {
                pointer = ts.getUpperBound();
            }
        }

        return formatted;
    }
    public Date getDateFrom12HourFormat(String twelve_hour_format){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE/MM/yyyy h:mm a");
        try {
            return sdf.parse(twelve_hour_format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd h:mm a");
        try {
            return sdf.parse(twelve_hour_format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TimeSet> getTodaysTimeSetList(Date day,HashMap<Date, Date> appointments) {
        ArrayList<Date> keys = new ArrayList<>(appointments.keySet());//get the keyset
        Collections.sort(keys);//order the key set ASC order
        ArrayList<TimeSet> taken_list = new ArrayList<>();///keep record of the kept times in 5:00pm-6:00pl
        for (Date start : keys) {
            if(Utils.isDayOrMoreAfter(start,day))return taken_list;//no need to continue if after also may not work bug..
            if (!Utils.isSameDay(day, start)) continue;//skipp if not same day
            Date end = appointments.get(start);
            TimeSet ts = new TimeSet(start,end);
            taken_list.add(ts);
        }//end of loop have all todays reservations
        return taken_list;
    }
    public static boolean isDayOrMoreAfter(Date date, Date today){
        return date.getDate()>today.getDate() && date.getMonth()>=today.getMonth() && date.getYear()>=today.getYear();
    }

    public static String formatPhoneNumber(String phone) {
        if(phone==null || phone.length()<10)return "N/A";
        String f=phone.substring(0,3);
        String ff = phone.substring(3,6);
        String fff =phone.substring(6,10);
        return "("+f+")"+ff+"-"+fff;
    }

    /**
     * Attempts to convert a datetime into a Date
     * @param start
     * @return
     */
    public static Date convertToWholeDay(Date start) {
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        return start;
    }
    public static Bitmap convertBytesToBitmap(byte[] pic){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
      //  options.inJustDecodeBounds =true;
        options.outHeight=200;
        options.outWidth =200;
        options.inScaled=true;
         Bitmap b =  BitmapFactory.decodeByteArray(pic, 0,pic.length, options);
        //options.inSampleSize = 8;
        //int imgHeight = options.outHeight;
        //int imgWidth= options.outWidth;
        //String imageType = options.outMimeType;

        return b;
    }
    public static String convertToString(byte[] arr){
        return Base64.encodeToString(arr,Base64.DEFAULT);
    }
    public static byte[] convertToByteArray(String arr){
        return Base64.decode(arr,Base64.DEFAULT);
    }
  /*  public static Bitmap decodeSampledBitmapFromArray(byte[]arr,int reqWidth,int reqHeight){
        final BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(arr,0,arr.length,opt);
        opt.inSampleSize = calculateInSampleSize(opt,reqWidth,reqHeight);
        opt.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(arr,0,arr.length,opt);
    }
    */
    public static byte[] convertBitmapToByteArray(Bitmap b){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG,0, stream);
        return stream.toByteArray();
    }
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        final int h = options.outHeight;
        final int w = options.outWidth;
        int inSampleSize = 1;
        if(h > reqHeight || w > reqHeight){
                final int halfHeight = h / 2;
                final int halfWidth = h / 2;
                while((halfHeight/inSampleSize)>= reqHeight && (halfWidth/inSampleSize)>= reqWidth){
                    inSampleSize *= 2;
                }
        }
        return inSampleSize;
    }

    /**
     * This method  querys firebase stores stores all the stores in a list then calculates thhe distance from the user and
     * determines whether the store is in bounds(in radius search)
     * @param myLoc
     * @param map
     * @param radius
     * @return
     */
    public static ArrayList<FirebaseStore> calculateDistance(Location myLoc, List<FirebaseStore> map, int radius) {
        ArrayList<FirebaseStore> list = new ArrayList<>();
        for(FirebaseStore store : map){//search keySet
            double dist = Distance.calculateDistance(myLoc.getLatitude(),myLoc.getLongitude(),store.getLocation().latitude,store.getLocation().longitude);
            if(dist <= radius) {
                store.setMiles_away(dist);
                list.add(store);
            }
        }
        Collections.sort(list);
        map.clear();
        return list;
    }
}

package app.reservation.acbasoftare.com.reservation.Utils;

import app.reservation.acbasoftare.com.reservation.App_Objects.LatLng;

/**
 * Created by user on 1/23/17.
 */
public class Distance
{
   /* public static void main (String[] args) throws java.lang.Exception
    {
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "M") + " Miles\n");
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "K") + " Kilometers\n");
        System.out.println(distance(32.9697, -96.80322, 29.46786, -98.53506, "N") + " Nautical Miles\n");
    }*/

    /**
     * Calculate the miles.
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @param //unit
     * @return
     */

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        /*if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }*/

        return (dist);
    }
    public static double calculateDistance(LatLng one, LatLng two){return calculateDistance(one.latitude,one.longitude,two.latitude,two.longitude);}
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts decimal degrees to radians						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
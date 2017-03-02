package app.reservation.acbasoftare.com.reservation.Utils;

/**
 * Created by user on 2016-11-12.
 */
public class UnitConversion {
   private static double conversionKMtoMile=0.621371; //1km to mile ~ 0.621371
    private static double conversionMileToKM=1.60934;//1 mile ~ 1.60934
    public static double convertMilesToKM(int miles){
        return miles*conversionMileToKM;
    }
    public static  double convertKMtoMiles(double km){
        return km*conversionKMtoMile;
    }
}

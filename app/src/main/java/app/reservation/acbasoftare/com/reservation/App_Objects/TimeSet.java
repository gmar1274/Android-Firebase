package app.reservation.acbasoftare.com.reservation.App_Objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by user on 2016-12-11.
 */
public class TimeSet implements Comparable{
    private Date lower_bound, upper_bound;
    private long delta_time;//in seconds
    /**
     *
     * @param date- the days date
     *            @param range - start-end AMPM
     * Set lowerBound to be the date time set to start and same with upper bound
     */
    public TimeSet(Date date,String range){
        lower_bound=date;
        String formmated_string = this.getDateFormat();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy h:mm a");
        String[] arr = range.split("-");
        try {
            lower_bound = sdf.parse(formmated_string+" "+arr[0]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            upper_bound = sdf.parse(formmated_string+" "+arr[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public TimeSet(Date lb, Date ub) {
        this.lower_bound = lb;
        this.upper_bound = ub;
    }

    /**
     *
     * @param start
     * @param delta_time -needs to be in milliseconds
     */
    public TimeSet(Date start, long delta_time) {
        this.lower_bound = start;
        this.upper_bound = new Date(start.getTime() + delta_time);
    }

    /**
     * @param ts - Set to determine if these two sets are seperate sets that dont intersect. Ex) [a,b][b,c][c,d]
     *           where [c,b] is the parameter to be determined.
     * @return
     */
    public boolean isDisjoint(TimeSet ts) {
        return isLowerBoundDisjoint(ts) || isUpperBoundDisjoint(ts);
    }

    /**
     * given d<c and c>d
     * @param ts - [a,d] and this.set=[d,c] where isLowerBoundDisjoint if d<a and c<=a =>> [d,c][a,d]
     * * @return
     */
    public boolean isLowerBoundDisjoint(TimeSet ts) {
        return (this.lower_bound.before(ts.lower_bound) && this.upper_bound.compareTo(ts.lower_bound) <= 0);
    }

    public boolean isUpperBoundDisjoint(TimeSet ts) {
        return (this.lower_bound.compareTo(ts.upper_bound) >= 1 && this.upper_bound.after(ts.upper_bound));
    }
    /*
myset[b,c]
set_total[a,d] where b>=a && c<=d
 */
    public boolean isSubSet(Date abs_lower_bounds, Date abs_upper_bounds) {
        return this.upper_bound.compareTo(abs_upper_bounds) <= 0 && this.lower_bound.compareTo(abs_lower_bounds) >= 0;

    }

    /**
     * Get 12 hour format toString Refernce to display
     * Looks the same as getTimeRangeFormat....
     * @return
     */
    public String get12HourFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");//12 hour format
        return sdf.format(lower_bound)+"-"+sdf.format(upper_bound);
    }
    public Date getUpperBound(){
        return this.upper_bound;
    }
    public Date getLowerBound(){
        return this.lower_bound;
    }

    public String getTimeRangeFormat() {

        if(lower_bound==null || upper_bound==null)return "{Null TimeSet either ub or lb}";
        SimpleDateFormat time = new SimpleDateFormat("h:mm a");
        return time.format(lower_bound)+"-"+time.format(upper_bound);
    }

    /**
     * getDateFormat of the current date which is the lower bound...
     * @return
     */
    public String getDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy");
        return sdf.format(lower_bound);
    }
    public String toString(){
        return this.getTimeRangeFormat();
    }
    @Override
    public boolean equals(Object obj) {
        TimeSet ts = (TimeSet)obj;
        return this.getTimeRangeFormat().compareTo(ts.getTimeRangeFormat())==0;
    }
    @Override
    public int hashCode() {
        return this.toString().hashCode() + this.getDateFormat().hashCode();
    }

    @Override
    public int compareTo(Object o) {
        TimeSet ts =(TimeSet) o;
        if(this.lower_bound.before(ts.getLowerBound()))return -1;
        if(this.lower_bound.after(ts.getLowerBound())){return 1;}
        return 0;
    }
    /**
     * FIREBASE GETTERS
     */
    public Date getLower_bound() {
        return lower_bound;
    }

    public Date getUpper_bound() {
        return upper_bound;
    }

    public long getDelta_time() {
        return delta_time;
    }//END FIREBASE GETTERS
    public TimeSet(){}
}

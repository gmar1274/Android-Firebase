package app.reservation.acbasoftare.com.reservation.App_Objects;

import java.math.BigDecimal;

/**
 * Created by user on 1/10/17.
 */
public class ACBAPackage {
    private String id;
    private BigDecimal price;//per month
    private SUBSCRIPTION type;
    private enum SUBSCRIPTION{MONTHLY,YEARLY}

    /**
     * TEST DEBUG
     */
    public void debug(){
        this.id = "abcddcba";
        this.price = new BigDecimal(49.99);
        this.type = SUBSCRIPTION.MONTHLY;

    }
    public ACBAPackage(){

    }
    public String getID(){
        return  this.id;
    }
    public BigDecimal getPrice(){
        return this.price;
    }
    public SUBSCRIPTION getSubscriptionType(){
        return this.type;
    }
    public String toString(){
        return  "ID: "+this.id;
    }
}

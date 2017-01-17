package app.reservation.acbasoftare.com.reservation.App_Objects;

import app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity;

import static app.reservation.acbasoftare.com.reservation.App_Activity.MainActivity.phone;
/**
 * Created by user on 12/12/16.
 */
public class Customer {
    private String name,id,phone,email;
    public Customer(){
        this.name=null;
        this.id=MainActivity.user_fb_profile == null? "-1":MainActivity.user_fb_profile.getId();
    }
    public Customer(String name ,String phone,String email){
        this.name=name;
        this.phone=phone;
        this.email=email;
    }
    public Customer(String name){
        this.name=name;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public void setEmail(String email){
        this.email= email;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getEmail(){
        return this.email;
    }
    public Customer(String name,String id){
        this.name=name;
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public  String getID(){
        return id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setID(String id){
        this.id=id;
    }
    public String toString(){
        return "Customer:{name:"+name+",phone:"+phone+",id:"+id+"}";
    }
}

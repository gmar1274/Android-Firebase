package app.reservation.acbasoftare.com.reservation.Interfaces;

import app.reservation.acbasoftare.com.reservation.App_Objects.StylistMessageMetaData;
import app.reservation.acbasoftare.com.reservation.App_Objects.UserMessageMetaData;

/**
 * Created by user on 3/23/17.
 * TOP CLASS PARENT FOR usermetadata and stylistmetadata
 */

public class MessagingMetaData {
    private UserMessageMetaData user;
    private StylistMessageMetaData sty;

    public MessagingMetaData(UserMessageMetaData user ,StylistMessageMetaData sty){
        if(user != null){
            this.user = user;
        }else{
            this.sty = sty;
        }
    }

    public String getStylistPhotoUri(){
        if(user != null){
            return this.user.getStylist_photo_uri();
        }else{
          return this.sty.getStylist_photo_uri();
        }
    }
    public String getClientPhotoUri(){
        if(user != null){
            return this.user.getClient_photo_uri();
        }else{
            return this.sty.getClient_photo_uri();
        }
    }
    public String client_id(){
        if(user != null){
            return this.user.getClient_id();
        }else{
            return this.sty.getClient_id();
        }
    }
    public String stylist_id(){
        if(user != null){
            return this.user.getStylist_id();
        }else{
            return this.sty.getStylist_id();
        }
    }
}

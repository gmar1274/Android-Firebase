package app.reservation.acbasoftare.com.reservation.App_Objects;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static android.R.attr.key;

/**
 * Created by user on 1/9/17.
 */
public class ParamPair<K,V> {
    public String key;
    public String value;
    public ParamPair(K key, V val){
        try {
            this.key = URLEncoder.encode(String.valueOf(key), "UTF-8");
            this.value =URLEncoder.encode(String.valueOf( val), "UTF-8");
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
     
    }
    public String getPostParameter(){
        return this.key+"="+this.value;
    }
}

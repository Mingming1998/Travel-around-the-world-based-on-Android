package tour.example.tour.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dream on 2017/5/30.
 */

public class ChatUtil {
    public static String setAddress(String message){
        String url="http://www.tuling123.com/openapi/api?key=61f48e49a3054298918d2d220c139bb1&info="+message;
        return url;
    }

    public static String parseJSON(String jsonData){
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            int code=jsonObject.getInt("code");
            if(code==100000){
                String text=jsonObject.getString("text");
                return text;
            }else if(code==200000){
                String url=jsonObject.getString("url");
                return url;
            }else if(code==302000){
                String list=jsonObject.getString("list");
                return list;
            }else if(code==308000){
                String list=jsonObject.getString("list");
                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

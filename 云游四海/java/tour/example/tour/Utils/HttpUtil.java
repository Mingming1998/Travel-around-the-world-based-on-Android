package tour.example.tour.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;

/**
 * Created by dream on 2017/5/30.
 */

public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection=null;
                try {
                    URL url=new URL(address);
                    httpURLConnection= (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    InputStream inputStream=httpURLConnection.getInputStream();
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                    StringBuffer response=new StringBuffer();
                    String line;
                    while((line=bufferedReader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(httpURLConnection!=null){
                        httpURLConnection.disconnect();
                    }
                }

            }
        }).start();
    }

    public static String[] parseJSON(String jsonData){
        String[] result=new String[3];
        try {
            JSONObject jsonObject=new JSONObject(jsonData);
            result[0]=jsonObject.getString("content");
            result[1]=jsonObject.getString("translation");
            result[2]=jsonObject.getString("fenxiang_img");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getHttpBitmap(){
        android.text.format.Time time=new android.text.format.Time("GTM+8");
        time.setToNow();
        int year=time.year;
        int month=time.month+1;
        int day=time.monthDay;
        String urlTime= String.valueOf(year);
        if(month<10){
            urlTime=urlTime+"-0"+String.valueOf(month);
        }else {
            urlTime=urlTime+"-"+String.valueOf(month);
        }
        if(day<10){
            urlTime=urlTime+"-0"+String.valueOf(day);
        }else {
            urlTime=urlTime+"-"+String.valueOf(day);
        }
        String URL_PATH="http://cdn.iciba.com/web/news/longweibo/imag/"+urlTime+".jpg";
        Bitmap bitmap=null;
        try {
            URL url=new URL(URL_PATH);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
            Log.d("hahah","hahah");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return bitmap;
    }
}

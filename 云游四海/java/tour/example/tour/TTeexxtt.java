package tour.example.tour;


import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TTeexxtt {
    private String API1 = "http://api.map.baidu.com/telematics/v3/travel_city?location=";
    private String API2 = "&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json";

    //http://api.map.baidu.com/telematics/v3/travel_city?location=&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json

    public static void main(String args[]){
        //String city = "青岛".toString();
        new MyWeather().execute("http://api.map.baidu.com/telematics/v3/travel_city?location=青岛&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json");
    }



    public static class MyWeather extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer stringBuffer = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;
                if (httpURLConnection.getResponseCode() == 200) {
                    inputStream = httpURLConnection.getInputStream();
                    //检测网络异常
                } else {
                    return "11";
                }
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                stringBuffer = new StringBuffer();
                String timp = null;
                while ((timp = bufferedReader.readLine()) != null) {
                    stringBuffer.append(timp);
                }
                inputStream.close();
                reader.close();
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("11")) {

                System.out.println("网络异常");
                //Toast.makeText(SpotActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                /*JSONObject object = new JSONObject(s);
                //JSONObject object1 = object.getJSONArray("result").getJSONObject(0);
                JSONObject result = object.getJSONObject("result");
                //JSONObject update = object1.getJSONObject("update");
                //JSONObject now = object1.getJSONObject("now");
                String cityname = result.getString("cityname");
                String jianshu = result.getString("abstract");

                String itineraries1 = result.getString("itineraries");
                String itineraries2 = itineraries1.getString("itineraries");

                String path = itineraries2.getString("path");
                String name = path.getString("name");*/


                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject Jiti = object1.getJSONArray("itineraries").getJSONObject(0);
                JSONArray paths=Jiti.getJSONArray("itineraries");
                if (paths.length()>0){
                    for(int i=0;i<paths.length();i++){
                        JSONObject job=paths.getJSONObject(i);
                        System.out.println(job.get("name"));
                        System.out.println(job.get("detail"));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

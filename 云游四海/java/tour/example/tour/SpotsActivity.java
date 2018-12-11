package tour.example.tour;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SpotsActivity extends AppCompatActivity {

    /*private TextView tv_cityname;
    private TextView tv_jianshu;*/

    private String API1 = "http://api.map.baidu.com/telematics/v3/travel_city?location=";
    private String API2 = "&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json";

    private String URL;

    Spots spot=new Spots();
    List<Spots> ls=new ArrayList<Spots>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spots);
        binID();
        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                new SpotsActivity.MyWeather().execute(API1 + city +API2);

            }
        });*/


        Intent intent=getIntent();
        String city = intent.getStringExtra("city");
        URL=API1+city+API2;
        new SpotsActivity.MyWeather().execute(API1+city+API2);

        System.out.println("3333333333333333333333333333333333");
        for(int i=0;i<ls.size();i++){
            System.out.println(ls.get(i).getJingdian()+","+ls.get(i).getJieshao());

        }

        jingdianAdapter ada=new jingdianAdapter(SpotsActivity.this,R.layout.lv_item,ls);
        System.out.println("5555555555555555555555555555555555555");
        ListView listView=(ListView)findViewById(R.id.lv);
        listView.setAdapter(ada);
    }
    class MyWeather2 extends AsyncTask<String, String, String> {
        @Override
        /*protected String doInBackground(String... strings) {
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

                Toast.makeText(SpotsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                //System.out.println("kaishi json2");
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject object2 = object1.getJSONObject("ticket_info");
                String price=(String)object2.get("price");

                String abs=(String)object1.get("abstract");
                String telephone=(String)object1.get("telephone");

                //tv_jianshu.setText("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);

                System.out.println("这里是222222222222222222222222222222222222222222222");
                System.out.println("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);
                spot.setJieshao("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }*/

        protected void onPreExecute() {
            super.onPreExecute();
            StringBuffer stringBuffer = null;
            try {
                URL url = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s=stringBuffer.toString();
            try {
                //System.out.println("kaishi json2");

                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject object2 = object1.getJSONObject("ticket_info");
                String price=(String)object2.get("price");

                String abs=(String)object1.get("abstract");
                String telephone=(String)object1.get("telephone");

                //tv_jianshu.setText("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);

                spot.setJieshao("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
    class MyWeather extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
        /*@Override
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

                //Toast.makeText(SpotsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                //System.out.println("kaishi json");
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject Jiti = object1.getJSONArray("itineraries").getJSONObject(0);
                JSONObject object2=Jiti.getJSONArray("itineraries").getJSONObject(0);
                JSONArray paths=object2.getJSONArray("path");
                String name="25125152";
                String detail="225925252";
                if (paths.length()>0){
                    for(int i=0;i<paths.length();i++){
                        JSONObject job=paths.getJSONObject(i);
                        name=(String) job.get("name");
                        detail=(String) job.get("detail");

                        System.out.println("这里是11111111111111111111111111111111111111111111");
                        System.out.println(name);
                        System.out.println(detail);

                        //tv_cityname.setText(" "+name);
                        spot.setJingdian(" "+name);
                        getXiangXi(detail);

                        ls.add(spot);
                        System.out.println("444444444444444444444444444444444444444");
                        //System.out.println("在JSON");
                    }
                }
                //tv_jianshu.setText(detail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }*/


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            StringBuffer stringBuffer = null;

            try {
                URL url = new URL(URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = null;

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            String s=stringBuffer.toString();
            if (s.equals("11")) {

                //Toast.makeText(SpotsActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                //System.out.println("kaishi json");
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject Jiti = object1.getJSONArray("itineraries").getJSONObject(0);
                JSONObject object2=Jiti.getJSONArray("itineraries").getJSONObject(0);
                JSONArray paths=object2.getJSONArray("path");
                String name="25125152";
                String detail="225925252";
                if (paths.length()>0){
                    for(int i=0;i<paths.length();i++){
                        JSONObject job=paths.getJSONObject(i);
                        name=(String) job.get("name");
                        detail=(String) job.get("detail");
                        //System.out.println(name);
                        //System.out.println(detail);

                        //tv_cityname.setText(" "+name);
                        spot.setJingdian(" "+name);


                        getXiangXi(detail);
                        ls.add(spot);
                        //System.out.println("在JSON");
                    }
                }

                //tv_jianshu.setText(detail);

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }


    private void binID() {
        /*tv_cityname = (TextView) findViewById(R.id.main_cityname);
        tv_jianshu = (TextView) findViewById(R.id.main_jianshu);*/
    }

    private void getXiangXi(String url){
        URL=url;
        new SpotsActivity.MyWeather2().execute(url);
    }
}

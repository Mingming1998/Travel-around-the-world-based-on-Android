package tour.example.tour;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class TwoSpotActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private TextView tv_cityname;
    private TextView tv_jianshu;

    private String API1 = "http://api.map.baidu.com/telematics/v3/travel_city?location=";
    private String API2 = "&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_spot);
        binID();
        Intent intent=getIntent();
        String city = intent.getStringExtra("city");
        new TwoSpotActivity.MyWeather().execute(API1 + city +API2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                new TwoSpotActivity.MyWeather().execute(API1 + city +API2);

            }
        });
    }
    class MyWeather2 extends AsyncTask<String, String, String> {
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

                Toast.makeText(TwoSpotActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                //System.out.println("kaishi json2");
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONObject object2 = object1.getJSONObject("ticket_info");
                String price=(String)object2.get("price");

                String abs=(String)object1.get("abstract");
                String telephone=(String)object1.get("telephone");

                tv_jianshu.setText("价格是"+price+"\n\n"+"这个地方"+abs+"\n\n"+"电话是"+telephone);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    class MyWeather extends AsyncTask<String, String, String> {
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
                        getXiangXi(detail);
                        //System.out.println("在JSON");
                    }
                }
                tv_cityname.setText(" "+name);
                tv_jianshu.setText(detail);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private void binID() {
        editText = (EditText) findViewById(R.id.main_editView);
        button = (Button) findViewById(R.id.main_button);
        tv_cityname = (TextView) findViewById(R.id.main_cityname);
        tv_jianshu = (TextView) findViewById(R.id.main_jianshu);

    }

    private void getXiangXi(String url){
        new TwoSpotActivity.MyWeather2().execute(url);
    }
}

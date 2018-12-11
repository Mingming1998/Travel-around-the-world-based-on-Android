package tour.example.tour;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class SpotActivity extends AppCompatActivity {
    private EditText editText;
    private ImageButton imageButtone;
    private Button detail;
    private Button most;
    private TextView tv_cityname;
    private TextView tv_jianshu;
    private TextView tv_description;
    private String API1 = "http://api.map.baidu.com/telematics/v3/travel_city?location=";
    private String API2 = "&ak=TueGDhCvwI6fOrQnLM0qmXxY9N0OkOiQ&output=json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);
        binID();
        imageButtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                new MyWeather().execute(API1 + city +API2);

            }
        });

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=editText.getText().toString();
                Intent intent = new Intent(SpotActivity.this, WebTourActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);

                /*strategy.instance.finish();
                Intent intent=new Intent(SpotActivity.this, strategy.class);
                startActivity(intent);
                finish();
*/
            }
        });
        most.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city=editText.getText().toString();
                Intent intent = new Intent(SpotActivity.this, TwoSpotActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);

                /*strategy.instance.finish();
                Intent intent=new Intent(SpotActivity.this, strategy.class);
                startActivity(intent);
                finish();
*/
            }
        });
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

                Toast.makeText(SpotActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject object = new JSONObject(s);
                //JSONObject object1 = object.getJSONArray("result").getJSONObject(0);
                JSONObject result = object.getJSONObject("result");
                //JSONObject update = object1.getJSONObject("update");
                //JSONObject now = object1.getJSONObject("now");
                String cityname = result.getString("cityname");
                String jianshu = result.getString("abstract");
                String description = result.getString("description");
                //String wendu = now.getString("tmp");
                //String fengli = now.getString("wind_dir");
                //String qiangdu = now.getString("wind_sc");
                tv_cityname.setText("Welcome to "+cityname+"!");
                tv_jianshu.setText(jianshu);
                tv_description.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


    private void binID() {
        editText = (EditText)findViewById(R.id.main_editView);
        imageButtone = (ImageButton)findViewById(R.id.main_button);
        detail = (Button)findViewById(R.id.details);
        most = (Button)findViewById(R.id.most);
        tv_cityname = (TextView)findViewById(R.id.main_cityname);
        tv_jianshu = (TextView)findViewById(R.id.main_jianshu);
        tv_description = (TextView)findViewById(R.id.main_description);
    }
}

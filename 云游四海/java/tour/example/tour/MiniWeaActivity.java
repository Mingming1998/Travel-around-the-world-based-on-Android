package tour.example.tour;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class MiniWeaActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText editText;
    private Button button;
    private Button shandong;
    private TextView tv_tianqi;
    private TextView tv_wendu;
    private TextView tv_fengli;
    private TextView tv_time;
    private String API = "https://free-api.heweather.com/s6/weather/now?key=11e895a6b3854f0fb49508eea65df6ca&location=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_wea);
        binID();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                new MyWeather().execute(API + city);

            }
        });
        shandong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MiniWeaActivity.this, getWeaActivity.class);
                startActivity(intent);
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

                Toast.makeText(MiniWeaActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
                JSONObject basic = object1.getJSONObject("basic");
                JSONObject update = object1.getJSONObject("update");
                JSONObject now = object1.getJSONObject("now");
                String city = basic.getString("location");
                String loc = update.getString("loc");
                String tianqi = now.getString("cond_txt");
                String wendu = now.getString("tmp");
                String fengli = now.getString("wind_dir");
                String qiangdu = now.getString("wind_sc");
                tv_tianqi.setText(tianqi);
                tv_wendu.setText(wendu + "℃");
                tv_fengli.setText(fengli + qiangdu + "级");
                tv_time.setText(loc);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


   private void binID() {
        imageView=(ImageView)findViewById(R.id.main_imageview);
        editText = (EditText)findViewById(R.id.main_editView);
        button = (Button)findViewById(R.id.main_button);
        shandong = (Button)findViewById(R.id.shandong_wea);
        tv_tianqi = (TextView)findViewById(R.id.main_tianqi);
        tv_fengli = (TextView)findViewById(R.id.main_fengli);
        tv_wendu = (TextView)findViewById(R.id.main_wendu);
       tv_time = (TextView)findViewById(R.id.main_time);
    }
}

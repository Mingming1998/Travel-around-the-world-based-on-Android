package tour.example.tour;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private String API = "https://free-api.heweather.com/s6/weather/now?key=11e895a6b3854f0fb49508eea65df6ca&location=";
    private DrawerLayout mDrawerlayout;
    private Button button;
    private TextView city;
    private NavigationView navView;
    private ImageView weather;
    //private TextView content;
    String city1="";
    Thread A;

    TextView text;
    TextView n,z;//名字和资料

    String userId;//用户id
    ArrayList<Task> al;//al

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent cc = getIntent();
        city1=MainActivity.city1;
        //   PP = cc.getStringExtra("dizhi");
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.fold);

        navView = (NavigationView) findViewById(R.id.nav_view);
        weather = (ImageView) findViewById(R.id.weather);
        //content = (TextView) findViewById(R.id.task);//备忘录
        city = (TextView) findViewById(R.id.city);
        text = (TextView) findViewById(R.id.txt1);//天气显示：风向温度等
        n = (TextView) findViewById(R.id.name);
        z = (TextView) findViewById(R.id.c1);

        city.setText("当前位置："+city1);//显示城市
        new MyWeather().execute(API + city1);//调用API


        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.c3:
                        Intent intent = new Intent(Main2Activity.this, RobotActivity.class);
                        startActivity(intent);
                        Main2Activity.this.finish();
                        break;
                    case R.id.c4:
                        Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent2);
                        break;
                    case R.id.c5:
                        Intent intent3 = new Intent(Main2Activity.this, login_activity.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent3);
                        break;
                    case R.id.c6:
                        Toast.makeText(Main2Activity.this, "请在手机内存卡中的qunawan目录下寻找kml文件，可配合GoogleEarth查看自己的详细轨迹。", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.c8:
                        Toast.makeText(Main2Activity.this, "请联系队长QQ：1838314897 反馈意见，感谢您的使用！", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        }
        collapsingToolbar.setTitle("备忘录");
  /*      if(loadBingpic() != null)
            Glide.with(this).load(loadBingpic()).into(weather);

*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.per);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDrawerlayout.openDrawer(GravityCompat.START);

            }
        });
        FloatingActionButton wr = (FloatingActionButton) findViewById(R.id.wr);
        wr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Main2Activity.this,EditTask.class);
                startActivity(intent);
                Main2Activity.this.finish();



            }
        });

        //备忘录查询
        /*List<Task> list = DataSupport.findAll(Task.class);
        if(list!=null) {
            for (Task a : list) {
                content.setText(a.getContent());
            }
        }*/
        SharedPreferences sp=getSharedPreferences("User", Activity.MODE_PRIVATE);
        userId=sp.getString("userId", "0");
        al=new ArrayList<Task>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql="select * from task where id='"+userId+"' order by time desc";
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()) {
                        al.add(new Task(rs.getString(3),rs.getTimestamp(4).toString()));//content,time
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                JDBC.close();
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        TaskAdapter adapter = new TaskAdapter(Main2Activity.this, R.layout.task_item,al);
        ListView listView = (ListView) findViewById(R.id.task);
        listView.setAdapter(adapter);


    }
//天气

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

                Toast.makeText(Main2Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
            try {
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONArray("HeWeather6").getJSONObject(0);
                JSONObject basic = object1.getJSONObject("basic");
                JSONObject now = object1.getJSONObject("now");
                String city = basic.optString("location");
                String tianqi = now.optString("cond_txt");
                String wendu = now.optString("tmp");
                String fengli = now.optString("wind_dir");
                String qiangdu = now.optString("wind_sc");
                text.setText( wendu+"℃\n"+tianqi+"天\n"+fengli+qiangdu+"级");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

}

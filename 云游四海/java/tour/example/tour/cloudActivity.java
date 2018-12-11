package tour.example.tour;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class cloudActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private Button beijing;
    private Button jinan;
    private Button qingdao;
    private Button gugong;
    private Button daminghu;
    private Button zhanqiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);

        editText=(EditText)findViewById(R.id.city_spot);
        button=(Button)findViewById(R.id.bt_city_spot);
        beijing=(Button)findViewById(R.id.beijing);
        jinan=(Button)findViewById(R.id.jinan);
        qingdao=(Button)findViewById(R.id.qingdao);
        gugong=(Button)findViewById(R.id.gugong);
        daminghu=(Button)findViewById(R.id.daminghu);
        zhanqiao=(Button)findViewById(R.id.zhanqiao);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String city_spot = editText.getText().toString();
                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content="+city_spot+"&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

                /*BaiduMapRoutePlan.setSupportWebRoute(true);
                BaiduMapPoiSearch.openBaiduMapPanoShow("65e1ee886c885190f60e77ff", cloudActivity.this); // 天安门*/
            }
        });
        beijing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=北京&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        jinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=济南&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        qingdao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=青岛&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });

        gugong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=故宫&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        daminghu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=大明湖&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
        zhanqiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Uri uri=Uri.parse("https://720yun.com/search?channelId=0&content=栈桥&page=1&selected=2&type=1");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }
}

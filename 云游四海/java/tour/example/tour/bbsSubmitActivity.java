package tour.example.tour;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class bbsSubmitActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    String userId;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_submit);

        editText=(EditText)findViewById(R.id.bbs_content);
        button=(Button)findViewById(R.id.bbs_submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp=getSharedPreferences("User", Activity.MODE_PRIVATE);
                userId=sp.getString("userId", "0");
                //Toast.makeText(bbsSubmitActivity.this,"bbs功能尚待开发",Toast.LENGTH_SHORT).show();
                /*String city_spot = editText.getText().toString();
                Intent intent = new Intent(cloudActivity.this,WebcloudActivity.class);
                intent.putExtra("city_spot", city_spot);
                startActivity(intent);*/

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String sql="insert into social set id='"+userId+"',time='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"',content='"+editText.getText().toString()+"',praise=0,talk=0";
                        i=JDBC.executeUpdate(sql);
                        JDBC.close();
                    }
                });

                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                if(i==1){
                    Toast.makeText(bbsSubmitActivity.this, "发表成功！", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(bbsSubmitActivity.this,RefreshActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(bbsSubmitActivity.this, "发表失败！", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

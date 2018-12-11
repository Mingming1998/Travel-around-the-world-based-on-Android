package tour.example.tour;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditTask extends AppCompatActivity {

    EditText et;
    TextView r,l;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        SharedPreferences sp=getSharedPreferences("User", Activity.MODE_PRIVATE);
        userId=sp.getString("userId", "0");
        System.out.println("fbjnjnjkvkbfjdvknjkfbvknfdkbvjkdfbkjfdbjknkjn lfdnkbnjb11h");
        System.out.println(userId);


        et = (EditText) findViewById(R.id.type);
        l = (TextView) findViewById(R.id.backback);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditTask.this,Main2Activity.class);

                startActivity(intent);
                EditTask.this.finish();
            }
        });
        r = (TextView) findViewById(R.id.ok);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Task t = new Task();
//                t.setContent(et.getText().toString());
//                t.save();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(userId);
                        System.out.println(userId);
                        System.out.println(userId);System.out.println(userId);
                        System.out.println("fbjnjnjkvkbfjdvknjkfbvknfdkbvjkdfbkjfdbjknkjn lfdnkbnjbh");


                        String sql="insert into task set id='"+userId+"',content='"+et.getText().toString()+"',time='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"'";
                        int i=JDBC.executeUpdate(sql);
                        if(i==1){
                            //android.widget.Toast.makeText(EditTask.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            System.out.println("你说不能弹出就不弹出吗？");
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
                Intent intent = new Intent(EditTask.this, Main2Activity.class);

                startActivity(intent);
                EditTask.this.finish();

            }
        });
    }
}

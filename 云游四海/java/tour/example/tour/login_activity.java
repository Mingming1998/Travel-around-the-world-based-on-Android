package tour.example.tour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

public class login_activity extends AppCompatActivity{

    Button login;
    Button c;
    Button free;
    TextView find ;
    EditText na,pa;
    String re=null;
    int i = 0;
    int exist=0;
    int frees=0;
    boolean dl=false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox remberPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        //获取SharedPreferences对象
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        login = (Button) findViewById(R.id.loginButton);
        na = (EditText) findViewById(R.id.username);
        pa = (EditText) findViewById(R.id.password);

        //调用getBoolean()方法去获取remember_password这个键对应的值, 如果不存在默认的值, 就是用的是false
        boolean isRemember = pref.getBoolean("remember_password", false);
        remberPass = (CheckBox)findViewById(R.id.remember_pass);

        if(isRemember){
            //账号和密码都设置到文本框
            String username = pref.getString("username","");
            String password = pref.getString("password","");

            na.setText(username);
            pa.setText(password);
            remberPass.setChecked(true);

        }

        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String username = na.getText().toString();
                String password = pa.getText().toString();
                try {
                    if(isequals()==1){

                        // 将数据存储在SharedPreferences当中
                        editor = pref.edit();
                        if(remberPass.isChecked()){ // 检验复选框是否被选中
                            // 如果被选中, remember_password的值改为True
                            editor.putBoolean("remember_password", true);
                            editor.putString("username",username);
                            editor.putString("password", password);
                        } else{
                            editor.clear();
                        }
                        editor.apply();

                        Toast.makeText(login_activity.this, "欢迎回来！", Toast.LENGTH_SHORT).show();
                        Intent inNN = new Intent(login_activity.this,MainActivity.class);
                        startActivity(inNN);
                        finish();
                    }else{
                        Toast.makeText(login_activity.this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        c = (Button) findViewById(R.id.button1);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_activity.this, rezhuce.class);
                intent.putExtra("zhuce","1");
                startActivity(intent);
            }
        });

        find = (TextView ) findViewById(R.id.textView1);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_activity.this, findpwdActivity.class);
                intent.putExtra("zhuce", "2");
                startActivity(intent);
            }
        });

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql="select * from user where username='zhangsan'and password='123'";
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()) {
                        frees = 1;
                        SharedPreferences spp=getSharedPreferences("User", Context.MODE_PRIVATE);
                        SharedPreferences.Editor et=spp.edit();
                        et.putString("userId",rs.getString(2));
                        et.commit();
                    }
                }catch (SQLException e){
                    e.printStackTrace();
                }
                JDBC.close();
            }
        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        free=(Button)findViewById(R.id.button2);
        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frees==1){
                    Intent inNN = new Intent(login_activity.this,MainActivity.class);
                    startActivity(inNN);
                    finish();
                }
            }
        });

        /*free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spp=getSharedPreferences("User", Context.MODE_PRIVATE);
                SharedPreferences.Editor et=spp.edit();
                et.putInt("userId",1);
                et.commit();
                Intent intent = new Intent(login_activity.this, MainActivity.class);
                intent.putExtra("zhuce","1");
                Toast.makeText(login_activity.this,"您现在使用游客登录！",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });*/
    }

    public int isequals() throws Exception{
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (na.getText().toString().trim()!=null&&pa.getText().toString().trim()!=null){
                    String sql="select * from user where username='"+na.getText().toString()+"'and password='"+pa.getText().toString()+"'";
                    ResultSet rs=JDBC.executeQuery(sql);
                    try {
                        while (rs.next()) {
                            exist = 1;

                            SharedPreferences spp=getSharedPreferences("User", Context.MODE_PRIVATE);
                            SharedPreferences.Editor et=spp.edit();
                            et.putString("userId",rs.getString(2));
                            et.commit();
                        }
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
                    JDBC.close();
                }else if(na.getText().toString().trim()==null){
                    Toast.makeText(login_activity.this, "请输入用户名！", Toast.LENGTH_SHORT).show();
                }else if(pa.getText().toString().trim()==null){
                    Toast.makeText(login_activity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return exist;
    }
}

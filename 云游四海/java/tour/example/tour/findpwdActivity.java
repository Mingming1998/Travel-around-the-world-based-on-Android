package tour.example.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

public class findpwdActivity extends AppCompatActivity {

    EditText findpwd_edit;
    Button findpwd;
    TextView textView;

    int exist=0;
    String pwd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpwd);
        findpwd_edit = (EditText) findViewById(R.id.findpwd_edit_name);
        findpwd = (Button) findViewById(R.id.findpwd_btn);
        findpwd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(eee()==1){
                    Toast.makeText(findpwdActivity.this, "您的密码为：'"+pwd+"'", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(findpwdActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textView = (TextView)findViewById(R.id.tv);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(findpwdActivity.this,rezhuce.class);
                startActivity(intent);
            }
        });
    }

    public int eee(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql="select * from user where username='"+findpwd_edit.getText().toString()+"'";
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()) {
                        exist = 1;
                        pwd = rs.getString("password");
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return exist;
    }

}

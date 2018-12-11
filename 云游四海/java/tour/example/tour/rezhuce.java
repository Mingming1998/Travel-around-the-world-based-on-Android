package tour.example.tour;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;

public class rezhuce extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private Button mSureButton;                       //确定按钮
    private Button mCancelButton;                     //取消按钮
    private int exist=0;
    private int i=0;
    boolean zc=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezhuce);
        mAccount = (EditText) findViewById(R.id.resetpwd_edit_name);
        mPwd = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
        mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);

        mSureButton = (Button) findViewById(R.id.register_btn_sure);
        mCancelButton = (Button) findViewById(R.id.register_btn_cancel);

        mSureButton.setOnClickListener(m_register_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_register_Listener);

    }
    View.OnClickListener m_register_Listener = new View.OnClickListener() {    //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.register_btn_sure:                       //确认按钮的监听事件
                    if(!zc){
                        register_check();
                        zc=true;
                        mSureButton.setBackgroundColor(Color.GRAY);
                    }
                    break;
                case R.id.register_btn_cancel:                     //取消按钮的监听事件,由注册界面返回登录界面
                    Intent intent_Register_to_Login = new Intent(rezhuce.this,login_activity.class) ;    //切换User Activity至Login Activity
                    startActivity(intent_Register_to_Login);
                    finish();
                    break;
            }
        }
    };
    public void register_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            final String userName = mAccount.getText().toString().trim();
            final String userPwd = mPwd.getText().toString().trim();
            final String userPwdCheck = mPwdCheck.getText().toString().trim();

            //检查用户是否存在
            Thread oneThread = new Thread(new Runnable() {
                @Override
                public synchronized void run() {
                        String sql="select * from user where username='"+mAccount.getText().toString()+"'and password='"+mPwd.getText().toString()+"'";
                        ResultSet rs=JDBC.executeQuery(sql);
                        try {
                            while (rs.next()) {
                                exist = 1;
                            }
                        }catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                });
            oneThread.start();
            try {
                oneThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //用户已经存在时返回，给出提示文字
            if(exist==1){
                Toast.makeText(this, "用户已存在，请直接登陆", Toast.LENGTH_SHORT).show();
                return ;
            }else if (exist!=1){
                if(userPwd.equals(userPwdCheck)==false){     //两次密码输入不一样
                    Toast.makeText(this, "两次密码输入不一样", Toast.LENGTH_SHORT).show();
                    return ;
                }else if (userPwd.equals(userPwdCheck)==true){

                    Thread towThread = new Thread(new Runnable() {
                        @Override
                        public synchronized void run() {
                            String sql="insert into user(username,password) values ('"+userName+"','"+userPwd+"');";
                            i = JDBC.executeUpdate(sql);
                            JDBC.close();
                        }
                    });
                    towThread.start();
                    try {
                        towThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(i==1){
                        Toast.makeText(rezhuce.this, "注册成功，请使用该账号登陆", Toast.LENGTH_SHORT).show();
                        Intent inNN = new Intent(rezhuce.this,login_activity.class);
                        startActivity(inNN);
                        finish();
                    }else{
                        Toast.makeText(rezhuce.this, "注册失败，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }else if(mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, "确认密码为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
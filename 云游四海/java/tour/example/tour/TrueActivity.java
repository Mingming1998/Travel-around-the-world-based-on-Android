package tour.example.tour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class TrueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true);


        new Handler().post(new Runnable() {
            @Override
            public void run() {

                Boolean isFirst = SharePreUtil.getBoolean(getApplicationContext(), ConstantValue.ISFIRST, true);
                if(isFirst){
                    //进入包含了viewpager那个导航界面
                    Intent intent = new Intent(getApplicationContext(), WelcomeGuideActivity.class);
                    startActivity(intent);
                    //将isFirst改为false,并且在本地持久化
                    SharePreUtil.saveBoolean(getApplicationContext(), ConstantValue.ISFIRST, false);
                }else{
                    //进入应用程序主界面
                    Intent intent = new Intent(getApplicationContext(), login_activity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

    }
}
 class SharePreUtil {

    private static SharedPreferences sp;

    /** 保存数据 **/
    public static void saveBoolean(Context ctx, String key, boolean value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /** 取出数据 **/
    public static Boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

}

interface ConstantValue {

    public static String ISFIRST = "isFirst";//全局静态变量
}
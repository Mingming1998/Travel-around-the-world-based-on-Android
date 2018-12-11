package tour.example.tour;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tour.example.tour.Utils.ChatUtil;
import tour.example.tour.Utils.HttpCallbackListener;
import tour.example.tour.Utils.HttpUtil;
import tour.example.tour.Utils.MsgAdapter;
import tour.example.tour.bean.Msg;


public class RobotActivity extends AppCompatActivity {

    private ListView msgListView;
    private Button button_Send;
    private MsgAdapter msgAdapter;
    private EditText editText;
    private String inputContent;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                msgList.add((Msg)msg.obj);
                msgAdapter.notifyDataSetChanged();
                msgListView.setSelection(msgList.size());
            }
        }
    };

    private List<Msg> msgList=new ArrayList<Msg>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
        initMsgs();
        initEvents();
    }

    private void initMsgs() {
        Msg m=new Msg("Hello guy.",Msg.TYPE_RECEIVED);
        msgList.add(m);
    }

    private void initEvents() {
        msgAdapter=new MsgAdapter(RobotActivity.this,R.layout.msg_item,msgList);
        msgListView.setAdapter(msgAdapter);

        button_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputContent=editText.getText().toString();
                if(!"".equals(inputContent)){
                    Msg m=new Msg(inputContent,Msg.TYPE_SENT);
                    msgList.add(m);
                    msgAdapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    editText.setText("");
                    HttpUtil.sendHttpRequest(ChatUtil.setAddress(inputContent), new HttpCallbackListener() {
                        @Override
                        public void onFinish(String response) {
                            Message message=new Message();
                            message.what=1;
                            Msg m=new Msg(ChatUtil.parseJSON(response),Msg.TYPE_RECEIVED);
                            message.obj=m;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onError(Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });

    }

    private void initViews() {
        editText= (EditText) findViewById(R.id.edit_text);
        msgListView= (ListView) findViewById(R.id.msgListView);
        button_Send= (Button) findViewById(R.id.imgButton);
    }
}


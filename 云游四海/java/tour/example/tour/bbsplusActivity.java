package tour.example.tour;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tour.example.tour.adapter.CommentExpandAdapter;
import tour.example.tour.bean.CommentBean;
import tour.example.tour.bean.CommentDetailBean;
import tour.example.tour.bean.ReplyDetailBean;
import tour.example.tour.view.CommentExpandableListView;

/**
 * by moos on 2018/04/20
 */
public class bbsplusActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "bbsplusActivity";
    private android.support.v7.widget.Toolbar toolbar;
    private TextView bt_comment;
    private CommentExpandableListView expandableListView;
    private CommentExpandAdapter adapter;
    private CommentBean commentBean;
    private List<CommentDetailBean> commentsList;
    private BottomSheetDialog dialog;

    private TextView name;
    private TextView time;
    private TextView content;

    String id;//发帖人
    String nu;//帖子

    String userId;
    int t;

    Thread thread1 = null;

    List<Integer> Anums=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbsplus);


        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        nu=intent.getStringExtra("num");
        Toast.makeText(bbsplusActivity.this,"点击查看评论...",Toast.LENGTH_SHORT).show();
        String tim=intent.getStringExtra("time");
        String cont=intent.getStringExtra("content");


        initView();

        SharedPreferences sp=getSharedPreferences("User", Activity.MODE_PRIVATE);
        userId=sp.getString("userId","0");


        name.setText(id);
        time.setText(tim);
        content.setText(cont);
    }

    private void initView() {
        name=(TextView) findViewById(R.id.detail_page_userName);
        time=(TextView) findViewById(R.id.detail_page_time);
        content=(TextView) findViewById(R.id.detail_page_story);

        //上滑朋友圈
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //列表添加
        expandableListView = (CommentExpandableListView) findViewById(R.id.detail_page_lv_comment);

        //发表动态
        bt_comment = (TextView) findViewById(R.id.detail_page_do_comment);
        bt_comment.setOnClickListener(this);
        //上滑朋友圈
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("评论详情");

        commentsList = generateTestData();
        //commentsList=new ArrayList<>();
        initExpandableListView(commentsList);
    }

    /**
     * 初始化评论和回复列表
     */
    private void initExpandableListView(final List<CommentDetailBean> commentList){
        expandableListView.setGroupIndicator(null);
        //默认展开所有回复
        adapter = new CommentExpandAdapter(this, commentList);
        expandableListView.setAdapter(adapter);
        for(int i = 0; i<commentList.size(); i++){
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int groupPosition, long l) {
                boolean isExpanded = expandableListView.isGroupExpanded(groupPosition);
                //Log.e(TAG, "onGroupClick: 当前的评论id>>>"+commentList.get(groupPosition).getId());

//                if(isExpanded){
//                    expandableListView.collapseGroup(groupPosition);
//                }else {
//                    expandableListView.expandGroup(groupPosition, true);
//                }
                showReplyDialog(groupPosition);
                return true;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                Toast.makeText(bbsplusActivity.this,"点击了回复",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //toast("展开第"+groupPosition+"个分组");

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.detail_page_do_comment){

            showCommentDialog();
        }
    }

    public List<CommentDetailBean> generateTestData(){

        final List<CommentDetailBean> lc=new ArrayList<>();

        thread1=new Thread(new Runnable() {
            @Override
            public void run(){
                String sql="select * from talkA where id="+Integer.parseInt(nu);
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()){
                        CommentDetailBean commentDetailBean=new CommentDetailBean(String.valueOf(rs.getString(3)),rs.getString(5),String.valueOf(rs.getTimestamp(4)));
                        lc.add(commentDetailBean);
//                       lc.add(new CommentDetailBean("name","content","time"));
                        Anums.add(rs.getInt(1));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                for(t=0;t<lc.size();t++){
                    final List<ReplyDetailBean> list=new ArrayList<>();
                    String sql1="select * from talkB where id="+nu+" and Anum="+Anums.get(t).intValue();
                    ResultSet rss=JDBC.executeQuery(sql1);
                    try {
                        while (rss.next()){
                            /*CommentDetailBean commentDetailBean=new CommentDetailBean(String.valueOf(rs.getInt(3)),rs.getString(5),String.valueOf(rs.getTimestamp(4)));
                            commentsList.add(commentDetailBean);*/
                            ReplyDetailBean replyDetailBean=new ReplyDetailBean(rss.getString(4),rss.getString(6));
                            list.add(replyDetailBean);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    lc.get(t).setReplyList(list);
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
        return lc;
    }

    /**
     * by moos on 2018/04/20
     * func:弹出评论框
     */
    private void showCommentDialog(){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        dialog.setContentView(commentView);
        /**
         * 解决bsd显示不全的情况
         */
        View parent = (View) commentView.getParent();
        BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
        commentView.measure(0,0);
        behavior.setPeekHeight(commentView.getMeasuredHeight());

        bt_comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final String commentContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(commentContent)){

                    //commentOnWork(commentContent);
                    dialog.dismiss();

                    CommentDetailBean detailBean = new CommentDetailBean(String.valueOf(userId), commentContent,String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
                    adapter.addTheCommentData(detailBean);

                    thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String sql="insert into talkA set id="+Integer.valueOf(nu)+",Aid='"+userId+"',time='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"',content='"+commentContent+"';";
                            int i=JDBC.executeUpdate(sql);
                            JDBC.close();
                        }
                    });
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(bbsplusActivity.this,"评论内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });

        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

    /**
     * by moos on 2018/04/20
     * func:弹出回复框
     */
    private void showReplyDialog(final int position){
        dialog = new BottomSheetDialog(this);
        View commentView = LayoutInflater.from(this).inflate(R.layout.comment_dialog_layout,null);
        final EditText commentText = (EditText) commentView.findViewById(R.id.dialog_comment_et);
        final Button bt_comment = (Button) commentView.findViewById(R.id.dialog_comment_bt);
        commentText.setHint("回复 " + commentsList.get(position).getNickName() + " 的评论:");
        dialog.setContentView(commentView);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String replyContent = commentText.getText().toString().trim();
                if(!TextUtils.isEmpty(replyContent)){

                    dialog.dismiss();
                    ReplyDetailBean detailBean = new ReplyDetailBean(String.valueOf(userId),replyContent);
                    adapter.addTheReplyData(detailBean, position);
                    expandableListView.expandGroup(position);

                    thread1 = new Thread(new Runnable() {
                        int Anum;
                        @Override
                        public void run() {
                            String sql="select * from talkA where Aid='"+commentsList.get(position).getNickName()+"' and time='"+commentsList.get(position).getCreateDate()+"' and content='"+commentsList.get(position).getContent().toString().trim()+"'";
                            ResultSet rs=JDBC.executeQuery(sql);
                            try{
                                while(rs.next()){
                                    Anum=rs.getInt(1);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            String sql2="insert into talkB set id="+Integer.valueOf(nu)+",Anum= "+Anum+",Bid='"+userId+"',time='"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"',content='"+replyContent+"';";
                            int ii=JDBC.executeUpdate(sql2);
                            try {
                                if(ii==1){
                                    // Toast.makeText(bbsplusActivity.this,"回复成功 "+sql2,Toast.LENGTH_SHORT).show();
                                }else{
                                    //Toast.makeText(bbsplusActivity.this,"未成功 "+sql2,Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
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

                }else {
                    Toast.makeText(bbsplusActivity.this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        commentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence) && charSequence.length()>2){
                    bt_comment.setBackgroundColor(Color.parseColor("#FFB568"));
                }else {
                    bt_comment.setBackgroundColor(Color.parseColor("#D8D8D8"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();
    }

}

package tour.example.tour;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.race604.flyrefresh.FlyRefreshLayout;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import tour.example.tour.FlyRefreshLayout;

public class RefreshActivity extends AppCompatActivity implements FlyRefreshLayout.OnPullRefreshListener,AdapterView.OnItemClickListener {

    private FlyRefreshLayout mFlylayout;
    private RecyclerView mListView;

    private csAdpater mAdapter;

    private ArrayList<ItemData> mDataSet = new ArrayList<>();
    private Handler mHandler = new Handler();
    private LinearLayoutManager mLayoutManager;
    List<ItemData> li=new ArrayList<ItemData>() ;
    int i,j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataSet();
        setContentView(R.layout.activity_refresh);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        System.out.println("");
        System.out.println("toolbar");
        mFlylayout = (FlyRefreshLayout) findViewById(R.id.fly_layout);
        System.out.println(" mFlylayout");
        mFlylayout.setOnPullRefreshListener(this);
        System.out.println("setOnPullRefreshListene");
        mListView = (RecyclerView) findViewById(R.id.list);
        System.out.println(" mListView");
        mLayoutManager = new LinearLayoutManager(this);
        mListView.setLayoutManager(mLayoutManager);
        mAdapter = new csAdpater(RefreshActivity.this,mDataSet);
        System.out.println("mLayoutManagerr");
        mListView.setAdapter(mAdapter);
        System.out.println("setAdapter");

        //调用方法,传入一个接口回调
        mAdapter.setItemClickListener(new csAdpater.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(RefreshActivity.this, "点击了" + position, Toast.LENGTH_SHORT).show();

                String num=(String)((TextView)view.findViewById(R.id.num)).getText();
                String id=(String)((TextView)view.findViewById(R.id.name)).getText();
                String time=(String)((TextView)view.findViewById(R.id.time)).getText();
                String content=(String)((TextView)view.findViewById(R.id.content)).getText();

                Intent intent=new Intent(RefreshActivity.this,bbsplusActivity.class);
                intent.putExtra("num",num);
                intent.putExtra("id",id);
                intent.putExtra("time",time);
                intent.putExtra("content",content);
                startActivity(intent);
            }
        });


        mListView.setItemAnimator(new SampleItemAnimator());
        System.out.println("setItemAnimator");

        //mListView.set

        View actionButton = mFlylayout.getHeaderActionButton();
        System.out.println("actionButton");


        if (actionButton != null) {
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFlylayout.startRefresh();
                    System.out.println("startRefresh(");
                }
            });
        }
    }

    //初始化
    private void initDataSet() {

//        mDataSet.add(new ItemData(R.mipmap.ic_assessment_white_24dp,0,1, Timestamp.valueOf("2010-02-10 11:10:18"),"你是笨蛋",1,5));
//        mDataSet.add(new ItemData(R.mipmap.ic_folder_white_24dp,1,2,Timestamp.valueOf("1013-11-10 10:19:10"),"你还是笨蛋",1,5));
//        mDataSet.add(new ItemData(R.mipmap.ic_folder_white_24dp,2,1,Timestamp.valueOf("1810-12-10 20:10:30"),"你依然是笨蛋",1,7));

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql="select * from social order by time desc";
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()) {
                            //头像图标，num，id，时间，内容，点赞数，评论数
                            ItemData itemData = new ItemData(R.mipmap.ic_folder_white_24dp,rs.getInt(1),rs.getString(2),rs.getTimestamp(3),rs.getString(4),rs.getInt(5),rs.getInt(6));
                            mDataSet.add(itemData);
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

    }

    //数据库调用添加
    private void addItemData() {
        Intent ii=new Intent(RefreshActivity.this,RefreshActivity.class);
        startActivity(ii);
        finish();
        j=0;
        i=mDataSet.size();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String sql="select * from social";
                ResultSet rs=JDBC.executeQuery(sql);
                try {
                    while (rs.next()) {
                        if(rs.getInt("num")>=i){
                            //头像图标，num，id，时间，内容，点赞数，评论数
                            ItemData itemData = new ItemData(R.mipmap.ic_folder_white_24dp,rs.getInt(1),rs.getString(2),rs.getTimestamp(3),rs.getString(4),rs.getInt(5),rs.getInt(6));

                            li.add(itemData);
                            j++;
                        }

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
//
        mLayoutManager.scrollToPosition(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent=new Intent(RefreshActivity.this,bbsSubmitActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh(FlyRefreshLayout view) {
        View child = mListView.getChildAt(0);
        if (child != null) {
            bounceAnimateView(child.findViewById(R.id.icon));
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFlylayout.onRefreshFinish();
            }
        }, 2000);
    }

    private void bounceAnimateView(View view) {
        if (view == null) {
            return;
        }

        Animator swing = ObjectAnimator.ofFloat(view, "rotationX", 0, 30, -20, 0);
        swing.setDuration(400);
        swing.setInterpolator(new AccelerateInterpolator());
        swing.start();
    }


    //刷新调用数据库
    @Override
    public void onRefreshAnimationEnd(FlyRefreshLayout view) {
        int i,ii,j;
        i=mDataSet.size();
        addItemData();
        ii=mDataSet.size();
        for(j=0;j<ii-i;j++){
            mDataSet.add(i, li.get(j));
            mAdapter.notifyItemInserted(j);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String num=(String)((TextView)view.findViewById(R.id.num)).getText();
        String id=(String)((TextView)view.findViewById(R.id.name)).getText();
        String time=(String)((TextView)view.findViewById(R.id.time)).getText();
        String content=(String)((TextView)view.findViewById(R.id.content)).getText();

        Intent intent=new Intent(RefreshActivity.this,bbsplusActivity.class);
        intent.putExtra("num",num);
        intent.putExtra("id",id);
        intent.putExtra("time",time);
        intent.putExtra("content",content);
        startActivity(intent);

    }
}

class csAdpater extends RecyclerView.Adapter<csAdpater.ViewHolder>  {
    private MyItemClickListener mItemClickListener;
    private Context mContext;
    private List<ItemData> mList;

    public csAdpater(RefreshActivity refreshActivity, List<ItemData> list) {
        this.mContext = refreshActivity;
        this.mList = list;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.view_list_item, null);
        //将全局的监听传递给holder
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //给空间赋值
        holder.icon.setImageResource(mList.get(position).icon);
        holder.num.setText(String.valueOf(mList.get(position).num));
        holder.name.setText(String.valueOf(mList.get(position).id));
        holder.time.setText(String.valueOf(mList.get(position).time));
        holder.content.setText(String.valueOf(mList.get(position).content));
        holder.praise.setText(String.valueOf(mList.get(position).praise));
        holder.talk.setText(String.valueOf(mList.get(position).talk));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView icon;//头像图标
        TextView num;//num
        TextView name;//用户名id
        TextView time;//时间
        TextView content;//内容
        TextView praise;//点赞数
        TextView talk;//评论数

        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            num = (TextView) itemView.findViewById(R.id.num);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);
            praise = (TextView) itemView.findViewById(R.id.praise);
            talk = (TextView) itemView.findViewById(R.id.talk);

            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}

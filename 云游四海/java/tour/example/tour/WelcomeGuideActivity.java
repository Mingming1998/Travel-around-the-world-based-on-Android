package tour.example.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class WelcomeGuideActivity extends AppCompatActivity {

    private ViewPager pager;
    private List<View> list;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);

        pager = (ViewPager) findViewById(R.id.welcome_pager);
        init();
        initViewPager();
    }

    public void init() {
        list = new ArrayList<View>();
    }

    //初始化ViewPager的方法
    public void initViewPager() {
        LayoutInflater inflater=LayoutInflater.from(this);//将每个xml文件转化为View
        View guideOne=inflater.inflate(R.layout.guidance01, null);//每个xml中就放置一个imageView
        View guideTwo=inflater.inflate(R.layout.guidance02,null);
        View guideThree=inflater.inflate(R.layout.guidance03,null);

        list.add(guideOne);//将view加入到list中
        list.add(guideTwo);
        list.add(guideThree);


        pager.setAdapter(new MyPagerAdapter());
        //监听ViewPager滑动效果
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //页卡被选中的方法
            @Override
            public void onPageSelected(int arg0) {
                //如果是第三个页面

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

                if (arg0 == 2) {

                    Intent intent = new Intent(WelcomeGuideActivity.this, login_activity.class);
                    startActivity(intent);
                    WelcomeGuideActivity.this.finish();
                }
            }


            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    //定义ViewPager的适配器
    class MyPagerAdapter extends PagerAdapter {
        //计算需要多少item显示
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        //初始化item实例方法
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        //item销毁的方法
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 注销父类销毁item的方法，因为此方法并不是使用此方法
//          super.destroyItem(container, position, object);
            container.removeView(list.get(position));
        }
    }



}


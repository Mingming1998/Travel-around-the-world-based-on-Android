package tour.example.tour;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;

import org.litepal.LitePal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity{


    //百度地图
    public LocationClient mLocationClient;
    public MapView mMapView;
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true;
    //轨迹
    int gatherInterval = 0;  //位置采集周期 (s)
    int packInterval = 5;  //打包周期 (s)
    String entityName = "tour";  // entity标识

    //鹰眼轨迹id
    long serviceId = 140932;// 鹰眼服务ID //鸣申请的204073，140932
    int traceType = 2;  //轨迹服务类型
    private static OnStartTraceListener startTraceListener = null;

    public static BDLocation location1;

    private AutoCompleteTextView keyWorldsView = null;
    private ArrayAdapter<String> sugAdapter = null;
    private int loadIndex = 0;
    private List<String> suggest;

    private static OnEntityListener entityListener = null;
    private RefreshThread refreshThread = null;  //刷新地图线程以获取实时点
    private static MapStatusUpdate msUpdate = null;
    private static BitmapDescriptor realtimeBitmap;  //图标
    private static OverlayOptions overlay;  //覆盖物
    private static List<LatLng> pointList = new ArrayList<LatLng>();  //定位点的集合
    private static PolylineOptions polyline = null;  //路线覆盖物
    int guiji = 0;
    int kaishi = 0;

    //kml
    //记录轨迹记录
    int simpleReturn = 0;
    // 是否纠偏
    int isProcessed = 0;
    // 纠偏选项
    String processOption = null;
    // 开始时间
    int startTime = 0;
    // 结束时间
    int endTime = 0;
    // 分页大小
    int pageSize = 5000;
    // 分页索引
    int pageIndex = 1;

    private List<Double> Lati = new ArrayList<Double>();
    ;

    private Trace trace;  // 实例化轨迹服务
    private LBSTraceClient client;  // 实例化轨迹服务客户端


    //权限
    List<String> permissionList;
    //按钮
    private ImageView Map;
    private ImageView Me;
    private ImageView Strategy;
    private ImageView Start;
    private ImageView Take;
    private ImageView Share;
    private ImageView Robot;
    private EditText editCity;
    private FrameLayout rootView;
    private AutoCompleteTextView searchkey;
    public static Double jing, wei;
    public static String J, W;
    private int judge = 1;
    public static String city = "";
    public static String city1 = "";
    public static String searchthing= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        String sdcardPath = Environment.getExternalStorageDirectory().toString();
        String mSampleDirPath = sdcardPath + "/云游四海/kml";
        File kml = new File(mSampleDirPath);
        if (!kml.exists()) {
            kml.mkdirs();
            //    System.out.println(kml.getAbsoluteFile());
        }

        //备忘录
        LitePal.getDatabase();

        mMapView = (MapView) findViewById(R.id.bmapView);//绑定xml中的mapview控件

        mMapView.removeViewAt(1); // 删除百度地图LoGo
        baiduMap = mMapView.getMap();//获取地图实例
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //测试按钮
        //移动到我的位置上
        baiduMap.setMyLocationEnabled(true);//开启定位图层
        //定位
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());

        searchkey = (AutoCompleteTextView)findViewById(R.id.searchkey);
        editCity = (EditText) findViewById(R.id.editCity);


        rootView = (FrameLayout) findViewById(R.id.fl_content);
        initBall();

        permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
        //

        //轨迹
        init();  //相关变量初始化

        initOnEntityListener();  //初始化实体监听器

        initOnStartTraceListener();  //初始化轨迹追踪监听器

        //

        //按钮
        Strategy = (ImageView) findViewById(R.id.strategy);
        Strategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocation();
                Intent intent = new Intent(MainActivity.this, StrActivity.class);
                NumberFormat ddf1 = NumberFormat.getNumberInstance();
                ddf1.setMaximumFractionDigits(2);
                J = ddf1.format(jing);
                W = ddf1.format(wei);
                intent.putExtra("jingdu", J);
                intent.putExtra("weidu", W);
                intent.putExtra("dizhi", city);
                startActivity(intent);
            }
        });
        Me = (ImageView) findViewById(R.id.me);
        Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("dizhi", city);
                System.out.println(city);
                startActivity(intent);
            }
        });
        Map = (ImageView) findViewById(R.id.map);
        Map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PoiSearchActivity.class);
                city = editCity.getText().toString();
                searchthing = searchkey.getText().toString();
                intent.putExtra("dizhi", city);
                intent.putExtra("sousuo",searchthing);
                System.out.println(city);
                startActivity(intent);
                Toast.makeText(MainActivity.this,"正在"+city+"查找"+searchthing,Toast.LENGTH_LONG).show();


                /*baiduMap.setMyLocationEnabled(true);
                mLocationClient.start();
                baiduMap.animateMapStatus();//动画的方式到中间

                Toast.makeText(MainActivity.this,"定位中，请稍后...",Toast.LENGTH_SHORT).show();*/

                /*Intent intent = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(intent);*/


            }
        });





        Start = (ImageView) findViewById(R.id.start);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*requestLocation();

                //开启之后调用baiduMap
                baiduMap.setMyLocationEnabled(true);

                if (guiji == 0) {
                    client.startTrace(trace, startTraceListener);
                    guiji = 1;
                    Date dataTime = new Date();
                    startTime = (int) dataTime.getTime();
                    Toast.makeText(MainActivity.this, "轨迹已经开启，并进行写入", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "写入开始", Toast.LENGTH_SHORT).show();
                } else {
                    guiji = 0;
                    Date dataTime = new Date();
                    endTime = (int) dataTime.getTime();
                    Toast.makeText(MainActivity.this, "轨迹已经关闭", Toast.LENGTH_SHORT).show();

                    client.stopTrace(trace, new OnStopTraceListener() {
                        // 轨迹服务停止成功
                        @Override
                        public void onStopTraceSuccess() {
                        }

                        // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
                        @Override
                        public void onStopTraceFailed(int arg0, String arg1) {
                            Toast.makeText(MainActivity.this, arg1 + "  " + arg0, Toast.LENGTH_SHORT).show();
                        }
                    });
                    baiduMap.clear();
                    //KML
                    Date time = new Date();
                    String line = System.getProperty("line.separator");
                    try {
                        //FileOutputStream out = openFileOutput("shiyan", Context.MODE_APPEND);
                        //  BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(out));
                        String sdcardPath = Environment.getExternalStorageDirectory().toString();
                        String mSampleDirPath = sdcardPath + "/云游四海/kml";
                        BufferedWriter bw = new BufferedWriter(new FileWriter(mSampleDirPath + time.toString() + ".kml"));
                        String qian = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" +
                                "  <Document>" +
                                "    <name>Paths</name>" +
                                "    <description>Examples of paths. Note that the tessellate tag is by default" +
                                "      set to 0. If you want to create tessellated lines, they must be authored" +
                                "      (or edited) directly in KML.</description>" +
                                "    <Style id=\"yellowLineGreenPoly\">" +
                                "      <LineStyle>" +
                                "        <color>7f00ffff</color>" +
                                "        <width>4</width>" +
                                "      </LineStyle>" +
                                "      <PolyStyle>" +
                                "        <color>7f00ff00</color>" +
                                "      </PolyStyle>" +
                                "    </Style>" +
                                "    <Placemark>" +
                                "      <name>Absolute Extruded</name>" +
                                "      <description>Transparent green wall with yellow outlines</description>" +
                                "      <styleUrl>#yellowLineGreenPoly</styleUrl>" +
                                "      <LineString>" +
                                "        <extrude>1</extrude>" +
                                "        <tessellate>1</tessellate>" +
                                "        <altitudeMode>2</altitudeMode>" +
                                "        <coordinates>";
                        String hou = " </coordinates>" + line +
                                "      </LineString>" + line +
                                "    </Placemark>" + line +
                                "  </Document>" + line +
                                "</kml>";


                        int m = 0;
                        int ss = 2357;
                        int num = Lati.size();
                        StringBuffer LL = new StringBuffer();
                        for (Double s : Lati) {
                            if (m % 2 == 0) {
                                LL.append(s + ",");
                            } else {
                                LL.append(s);
                                LL.append("," + ss);
                                LL.append("    ");

                            }
                            ++m;
                        }
                        String zhongjie = qian + LL + hou;
                        System.out.println("diyibu");
                        bw.write(zhongjie);
                        System.out.println("dierbu");
                        Toast.makeText(MainActivity.this, "写入完成", Toast.LENGTH_SHORT).show();
                        System.out.println("diysanbu");
                        bw.flush();
                        if (bw != null) {
                            bw.close();
                        }
                        if (Lati.size() < 10) {
                            Toast.makeText(MainActivity.this, "kon", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                showAddDialog();
            }
        });
        Take = (ImageView) findViewById(R.id.take);
        Take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rect rect = new Rect(0, 0, 30000, 30000);// 左xy 右xy
                baiduMap.snapshotScope(rect, new BaiduMap.SnapshotReadyCallback() {

                    @Override
                    public void onSnapshotReady(Bitmap snapshot) {
                        String sdcardPath2 = Environment.getExternalStorageDirectory().toString();
                        String mSampleDirPath2 = sdcardPath2 + "/云游四海/";
                        Date aa = new Date();
                        File file = new File(mSampleDirPath2 + aa.toString() + ".png");
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file);
                            if (snapshot.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                out.flush();
                                out.close();
                            }
                            Toast.makeText(MainActivity.this, "屏幕截图成功，图片存在: " + file.toString(),
                                    Toast.LENGTH_SHORT).show();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                });
                Toast.makeText(MainActivity.this, "正在截取屏幕图片...", Toast.LENGTH_SHORT).show();

            }

            ;
        });
        Share = (ImageView) findViewById(R.id.share);
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    Intent intent = new Intent(MainActivity.this, shareActivity.class);
                    intent.putExtra("weizhi", location1);
                    startActivity(intent);


            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (judge == 1) {
                    //Map.setVisibility(View.GONE);
                    Me.setVisibility(View.GONE);
                    //Strategy.setVisibility(View.GONE);
                    Start.setVisibility(View.GONE);
                    Take.setVisibility(View.GONE);
                    Share.setVisibility(View.GONE);
                    judge = 0;
                } else {
                    //Map.setVisibility(View.VISIBLE);
                    Me.setVisibility(View.VISIBLE);
                    //Strategy.setVisibility(View.VISIBLE);
                    Start.setVisibility(View.VISIBLE);
                    Take.setVisibility(View.VISIBLE);
                    Share.setVisibility(View.VISIBLE);
                    judge = 1;
                }
            }
        });

    }

//鹰眼1
    private void requestLocation() {
        //自动定位
        initLocation();
        mLocationClient.start();

    }
//鹰眼2
    private void initLocation() {
        /*LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);*/

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps

        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    //百度地图
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "为了更好地享受服务，请同意请求的所有权限！",
                                    Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "糟糕！发生了未知错误！", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }


    //定位监听
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {


            location1=location;
            //获取定位结果
            StringBuffer sb = new StringBuffer(256);

            sb.append("time : ");
            sb.append(location.getTime());    //获取定位时间

            sb.append("\nerror code : ");
            sb.append(location.getLocType());    //获取类型类型

            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());//获取纬度信息
            wei = location.getLatitude();
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            jing = location.getLongitude();
            //获取经度信息
            sb.append("\nradius : ");
            sb.append(location.getRadius());    //获取定位精准度

            if (location.getLocType() == BDLocation.TypeGpsLocation) {

                // GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());    // 单位：公里每小时

                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());    //获取卫星数

                sb.append("\nheight : ");
                sb.append(location.getAltitude());    //获取海拔高度信息，单位米

                sb.append("\ndirection : ");
                sb.append(location.getDirection());    //获取方向信息，单位度

                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                city1 = location.getDistrict();



                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

                // 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());    //获取地址信息

                city1 = location.getDistrict();  //获取具体县名


                sb.append("\noperationers : ");
                sb.append(location.getOperators());    //获取运营商信息

                sb.append("\ndescribe : ");
                sb.append("网络定位成功");

            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

                // 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");

            } else if (location.getLocType() == BDLocation.TypeServerError) {

                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");

            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

            }

            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());    //位置语义化信息


            //poi数据
            List<Poi> list = location.getPoiList();    // POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }

            Log.i("BaiduLocationApiDem", sb.toString());
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLocate) {
                isFirstLocate = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        @Override
        public void onConnectHotSpotMessage(String a, int b) {

        }
    }
    /*

     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    //轨迹
    private void init() {


        trace = new Trace(getApplicationContext(), serviceId, entityName, traceType);
        client = new LBSTraceClient(getApplicationContext());
        // 设置采集和打包周期
        client.setInterval(gatherInterval, packInterval);
// 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
// 设置http协议类型
        client.setProtocolType(1);
    }


    //机器人初始化
    private void initBall() {
        FloatBall floatBall = new FloatBall.Builder(this, rootView)
                .setBottomMargin(1440)
                .setRightMargin(0)
                .setHeight(160)
                .setWidth(160)
                .setRes(R.drawable.rob4)
                .setDuration(500)
                .setBall(new ImageView(this))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, RobotActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this,"开启机器人",Toast.LENGTH_LONG).show();
                    }
                })
                .build();
    }


    /**
     * 初始化设置实体状态监听器
     */
    private void initOnEntityListener() {

        //实体状态监听器
        entityListener = new OnEntityListener() {

            public void onRequestFailedCallback(String arg0) {
                Looper.prepare();
                Toast.makeText(
                        getApplicationContext(),
                        "entity请求失败的回调接口信息：" + arg0,
                        Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
            }


            public void onQueryEntityListCallback(String arg0) {
                /**
                 * 查询实体集合回调函数，此时调用实时轨迹方法
                 */
                showRealtimeTrack(arg0);
            }

        };
    }

    /**
     * 追踪开始
     */
    private void initOnStartTraceListener() {

        // 实例化开启轨迹服务回调接口
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）

            public void onTraceCallback(int arg0, String arg1) {
                Log.i("TAG", "onTraceCallback=" + arg1);
                if (arg0 == 0 || arg0 == 10006) {
                    startRefreshThread(true);
                }
            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）

            public void onTracePushCallback(byte arg0, String arg1) {
                Log.i("TAG", "onTracePushCallback=" + arg1);
            }
        };

    }


    /**
     * 轨迹刷新线程
     *
     * @author BLYang
     */
    private class RefreshThread extends Thread {

        protected boolean refresh = true;

        public void run() {

            while (refresh) {
                queryRealtimeTrack();
                try {
                    Thread.sleep(packInterval * 1000);
                } catch (InterruptedException e) {
                    System.out.println("线程休眠失败");
                }
            }

        }
    }


    /**
     * 启动刷新线程
     *
     * @param isStart
     */
    private void startRefreshThread(boolean isStart) {

        if (refreshThread == null) {
            refreshThread = new RefreshThread();
        }

        refreshThread.refresh = isStart;

        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
                try {
                    refreshThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            refreshThread = null;
        }

    }

    /**
     * 查询实时线路
     */
    private void queryRealtimeTrack() {

        String entityName = this.entityName;
        String columnKey = "";
        int returnType = 0;
        int activeTime = 0;
        int pageSize = 10;
        int pageIndex = 1;

        this.client.queryEntityList(
                serviceId,
                entityName,
                columnKey,
                returnType,
                activeTime,
                pageSize,
                pageIndex,
                entityListener
        );

    }

    /**
     * 展示实时线路图
     *
     * @param realtimeTrack
     */
    protected void showRealtimeTrack(String realtimeTrack) {

        if (refreshThread == null || !refreshThread.refresh) {
            return;
        }

        //数据以JSON形式存取
        RealtimeTrackData realtimeTrackData = GsonService.parseJson(realtimeTrack, RealtimeTrackData.class);

        if (realtimeTrackData != null && realtimeTrackData.getStatus() == 0) {

            LatLng latLng = realtimeTrackData.getRealtimePoint();

            if (latLng != null) {
                pointList.add(latLng);
                drawRealtimePoint(latLng);
                //  double x = latLng.longitude - 0.0065;
                //double y = latLng.latitude- 0.006;
                double x = latLng.latitude - 0.0065;
                double y = latLng.longitude - 0.006;
                double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
                double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
                double lng = z * Math.cos(theta);
                double lat = z * Math.sin(theta);
                Lati.add(lat);
                Lati.add(lng);
            } else {
                Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();
            }

        }

    }

    /**
     * 画出实时线路点
     *
     * @param point
     */
    private void drawRealtimePoint(LatLng point) {

        baiduMap.clear();
        MapStatus mapStatus = new MapStatus.Builder().target(point).zoom(18).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        realtimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.lc);
        overlay = new MarkerOptions().position(point)
                .icon(realtimeBitmap).zIndex(9).draggable(true);

        if (pointList.size() >= 2 && pointList.size() <= 1000) {
            polyline = new PolylineOptions().width(10).color(Color.RED).points(pointList);
        }

        addMarker();

    }

    private void addMarker() {

        if (msUpdate != null) {
            baiduMap.setMapStatus(msUpdate);
        }

        if (polyline != null) {
            baiduMap.addOverlay(polyline);
        }

        if (overlay != null) {
            baiduMap.addOverlay(overlay);
        }

    }


    private String getSDCardPath() {

        File sdcardDir = null;
//判断SDCard是否存在
        boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdcardExist) {

            sdcardDir = Environment.getExternalStorageDirectory();


        } else {
            Toast.makeText(MainActivity.this, "sd卡不存在!", Toast.LENGTH_SHORT).show();
        }

        return sdcardDir.toString();

    }


    public void startRoutePlanWalking( String end) {
        LatLng ptStart = new LatLng(location1.getLatitude(), location1.getLongitude());

        // 构建 route搜索参数
        RouteParaOption para = new RouteParaOption()
                .startPoint(ptStart)
//          .startName("天安门")
//          .endPoint(ptEnd);
                .endName(end);
               // .cityName(city);


        try {
            BaiduMapRoutePlan.openBaiduMapWalkingRoute(para, this);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
    protected void showAddDialog() {


        LayoutInflater factory = LayoutInflater.from(this);

        final View textEntryView = factory.inflate(R.layout.dialog, null);


        final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);
        final Button button1 = (Button) textEntryView.findViewById(R.id.button1);

        AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);

        ad1.setTitle("导航:");

        ad1.setIcon(android.R.drawable.ic_dialog_info);

        ad1.setView(textEntryView);



        ad1.show();// 显示对话框
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city4 = editTextNumEditText.getText().toString();
                startRoutePlanWalking(city4);

            }
        });


    }


}



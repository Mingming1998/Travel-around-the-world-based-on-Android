package tour.example.tour;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.File;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 项目名称：ShareDome
 * 类描述：
 * 创建人：xiaolijuan
 * 创建时间：2016/1/13 23:48
 */
public class shareActivity extends Activity implements View.OnClickListener {
    private RelativeLayout mRlShareText, mRlShareSingleimage, mRlShareMultipleimage;
    private int flag=0;
    private Button photo;
    private Button select;
    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;
    public String path1;
    public String uri1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share2);

        photo = (Button) findViewById(R.id.photo_button);
        select = (Button) findViewById(R.id.select_button);
        bindView();
        init();
        initListener();
    }

    private void bindView() {
        mRlShareText = (RelativeLayout) findViewById(R.id.rl_share_text);
        mRlShareSingleimage = (RelativeLayout) findViewById(R.id.rl_share_singleimage);



        mRlShareText.setOnClickListener(new ShareText());
        mRlShareSingleimage.setOnClickListener(new ShareSingleImage());

    }

    //分享文字至所有第三方软件
    public class ShareText implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

            intent.setAction(Intent.ACTION_SEND);

            intent.putExtra(Intent.EXTRA_TEXT, "定位时间：\n"+MainActivity.location1.getTime()+"\n纬度：\n"+MainActivity.location1.getLatitude()+"\n经度：\n"+MainActivity.location1.getLongitude()+"\n地理位置：\n"+MainActivity.location1.getAddrStr());

            intent.setType("text/plain");

            //设置分享列表的标题，并且每次都显示分享列表

            startActivity(Intent.createChooser(intent, "分享到"));
        }
    }

    //分享单张图片至所有第三方软件
    public class ShareSingleImage implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // String imagePath = Environment.getExternalStorageDirectory() + File.separator + "13e277bb0b9c2e3ab90229463357bf40.jpg";
            //由文件得到uri
            //Uri imageUri = Uri.fromFile(new File(imagePath));
            Intent imageIntent = new Intent(Intent.ACTION_SEND);
            imageIntent.setType("image/jpeg");
            imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path1));
//            if(path1!=null){
                startActivity(Intent.createChooser(imageIntent, "分享"));
          /*  }else{
                Toast.makeText(shareActivity.this,"请选择一张图片",Toast.LENGTH_SHORT).show();
            }*/


        }
    }



    @Override
    public void onClick(View v) {

    }
    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调

                path1 = outputFile.getAbsolutePath();

                uri1 = outputUri.toString();
                //    Glide.with(shareActivity.this).load(outputUri).into(mIvPic);

            }
        }, false);//true裁剪，false不裁剪

        //        mLqrPhotoSelectUtils.setAuthorities("com.lqr.lqrnativepicselect.fileprovider");
        //        mLqrPhotoSelectUtils.setImgPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg");
    }

    private void initListener() {
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、调用拍照方法
                PermissionGen.with(shareActivity.this)
                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                        ).request();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 3、调用从图库选取图片方法
                PermissionGen.needPermission(shareActivity.this,
                        LQRPhotoSelectUtils.REQ_SELECT_PHOTO,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}
                );
            }
        });
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void takePhoto() {
        mLqrPhotoSelectUtils.takePhoto();
    }

    @PermissionSuccess(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void selectPhoto() {
        mLqrPhotoSelectUtils.selectPhoto();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
    private void showTip1() {
        showDialog();
    }

    @PermissionFail(requestCode = LQRPhotoSelectUtils.REQ_SELECT_PHOTO)
    private void showTip2() {
        showDialog();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }

    public void showDialog() {
        //创建对话框创建器
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置对话框显示小图标
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置标题
        builder.setTitle("权限申请");
        //设置正文
        builder.setMessage("在设置-应用-虎嗅-权限 中开启相机、存储权限，才能正常使用拍照或图片选择功能");

        //添加确定按钮点击事件
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {//点击完确定后，触发这个事件

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这里用来跳到手机设置页，方便用户开启权限
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + shareActivity.this.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //添加取消按钮点击事件
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        //使用构建器创建出对话框对象
        AlertDialog dialog = builder.create();
        dialog.show();//显示对话框
    }
}


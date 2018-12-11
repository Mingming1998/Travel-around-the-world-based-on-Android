package tour.example.tour;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebcloudActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcloud);

        Intent intent = getIntent();
        String city_spot = intent.getStringExtra("city_spot");


        //通过导入TextPinyinUtil包，对汉字进行拼音转换，因为URL中只识别拼音
        //String citypinyin = TextPinyinUtil.getInstance().getPinyin(city);

        webView=(WebView) findViewById(R.id.webview);

        /* 设置支持Js,必须设置的,不然网页基本上不能看 */
        webView.getSettings().setJavaScriptEnabled(true);
        /* 这个不用说了,重写WebChromeClient监听网页加载的进度,从而实现进度条 */
        webView.setWebChromeClient(new WebChromeClient());
        /* 同上,重写WebViewClient可以监听网页的跳转和资源加载等等... */
        webView.setWebViewClient(new WebViewClient());


        //加载不出来全景地图
        webView.getSettings().setLoadsImagesAutomatically(true); // 加载图片

        webView.loadUrl("https://720yun.com/search?channelId=0&content="+city_spot+"&page=1&selected=2&type=1");
    }
}

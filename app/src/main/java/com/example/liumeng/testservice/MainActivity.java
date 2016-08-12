package com.example.liumeng.testservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    WebView webView;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=(WebView)findViewById(R.id.webview);
        webView.setWebChromeClient(new WebChromeClient());
        //允许JavaScript执行
        webView.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        webView.loadUrl("file:///android_asset/www/index.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
        webView.addJavascriptInterface(new Contact(), "contact");
    }

    public final class Contact {
        //JavaScript调用此方法拨打电话
        @JavascriptInterface
        public void call() {
            Log.v("success","start a service");
        }
        @JavascriptInterface
        public void startSocketService(String name,String _id){
            Intent intent =new Intent();
            intent.setAction("com.example.liumeng.testservice.ChatService");
            intent.putExtra("username",name);
            intent.putExtra("_id",_id);
            startService(intent);
        }
    }
}

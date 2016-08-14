package com.example.liumeng.testservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends Activity {
    WebView webView;
    Button button;
    private Socket mSocket;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        //mSocket.connect();
        webView=(WebView)findViewById(R.id.webview);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new onMyClickListener());
        webView.setWebChromeClient(new WebChromeClient());
        //允许JavaScript执行
        webView.getSettings().setJavaScriptEnabled(true);
        //找到Html文件，也可以用网络上的文件
        webView.loadUrl("file:///android_asset/www/index.html");
        // 添加一个对象, 让JS可以访问该对象的方法, 该对象中可以调用JS中的方法
        webView.addJavascriptInterface(new Contact(), "contact");
    }

    class onMyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Log.v("success","click");

            String username="dreams";
            String _iid="123";

            JSONObject json=new JSONObject();
            try{
                json.put("name",username);
                json.put("_id",_iid);
                mSocket.emit("login", json);
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public final class Contact {
        //JavaScript调用此方法拨打电话
        @JavascriptInterface
        public void call() {
            Log.v("success","start a service");
        }
        @JavascriptInterface
        public void startSocketService(String name,String _id){
            Log.v("success","start a service");
            /*

            Intent intent =new Intent(MainActivity.this,ChatService.class);
            intent.putExtra("username",name);
            intent.putExtra("_id",_id);
            startService(intent);
            */
            String username="dreams";
            String _iid="123";

            JSONObject json=new JSONObject();
            try{
                json.put("name",username);
                json.put("_id",_iid);
                mSocket.emit("login", json);
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
}

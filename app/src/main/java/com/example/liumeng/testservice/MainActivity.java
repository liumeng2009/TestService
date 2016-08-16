package com.example.liumeng.testservice;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;


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
    SQLiteDatabase db;
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("project dic",this.getFilesDir().toString());
        db=SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/sfDB.db3",null);


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
        public void startSocketService(String name,String _id,String image,String token){

            //判断表是否存在
            boolean tableExist=DbTools.isExistTable(db,"users");
            if(!tableExist){
                //不存在表，就建立一个users表
                db.execSQL("create table users(_id varchar(255) primary key,name varchar(50),active int,image blob,token blob)");
            }
            db.beginTransaction();
            try{
                //将已经存在的user激活状态全部置否
                ContentValues values=new ContentValues();
                values.put("active",false);
                db.update("users",values,null,null);
                //新值变成激活状态
                String sqlUserExist="select * from users where name='"+name+"'";
                Cursor cursor1=db.rawQuery(sqlUserExist,null);
                if(cursor1.getCount()!=0){
                    //存在的话，就把这个user激活
                    ContentValues cv=new ContentValues();
                    cv.put("active",1);
                    db.update("users",cv,"name=?",new String[]{name});
                }
                else{
                    //不存在，就新增新数据
                    ContentValues cv=new ContentValues();
                    cv.put("_id",_id);
                    cv.put("name",name);
                    cv.put("active",1);
                    cv.put("image",image);
                    cv.put("token",token);
                    db.insert("users",null,cv);
                }
                db.setTransactionSuccessful();
            }
            catch (SQLiteException e){
                Toast.makeText(MainActivity.this,R.string.error_sqlite, Toast.LENGTH_LONG).show();
            }
            db.endTransaction();
            db.close();
            //启动服务
            Intent intent =new Intent(MainActivity.this,ChatService.class);
            intent.putExtra("username",name);
            intent.putExtra("_id",_id);
            startService(intent);
        }
    }
}

package com.example.liumeng.testservice;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.liumeng.socketio.*;

public class ChatService extends Service {

    public static final String TAG="ChatService";
    static final int NOTIFICATION_ID=0x123;
    NotificationManager nm;

    private Socket mSocket;


    public ChatService() {

    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG, "onCreate() executed");

    /*
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent=new Intent(this,MainActivity.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setAutoCancel(true)
            .setTicker("新消息")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("一条新消息")
            .setContentText("这是消息的内容")
            .setDefaults(Notification.DEFAULT_SOUND)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pi);
        nm.notify(NOTIFICATION_ID,builder.getNotification());
        */
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.d(TAG, "onStartCommand() executed");

        String username=intent.getStringExtra("username");
        String _id=intent.getStringExtra("_id");

        ChatApplication app = (ChatApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on("loginsuccess", onLogin);
        JSONObject json=new JSONObject();
        try{
            json.put("name",username);
            json.put("_id",_id);
            mSocket.emit("login", json);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];

            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }

            //Intent intent = new Intent();
            //intent.putExtra("username", mUsername);
            //intent.putExtra("numUsers", numUsers);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    };
}

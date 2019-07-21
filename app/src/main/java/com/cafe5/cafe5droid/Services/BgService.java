package com.cafe5.cafe5droid.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.cafe5.cafe5droid.Classes.CDatabase;
import com.cafe5.cafe5droid.Classes.CPref;
import com.cafe5.cafe5droid.Classes.CSocketClientTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class BgService extends Service {
    private static Timer timer = new Timer();
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        timer.schedule(new TT(), 0, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    private class TT extends TimerTask {
        @Override
        public void run() {
            Log.d("TIMER", "YO!");
            JSONObject o = new JSONObject();
            try {
                o.put("cmd", CSocketClientTask.cmd_messagelist);
                o.put("lastmessage", CPref.lastMessage);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }
            CSocketClientTask sc = new CSocketClientTask();
            sc.strData = o.toString();
            sc.svr = BgService.this;
            sc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void handleMessage(JSONObject o) {
        Log.d("JSON SERVICE", o.toString());
        try {
            int cmd = o.getInt("cmd");
            switch (cmd) {
                case CSocketClientTask.cmd_messagelist:
                    JSONArray ja = o.getJSONArray("list");
                    int last = CPref.lastMessage;
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject mo = ja.getJSONObject(i);
                        last = last < mo.getInt("f_id") ? mo.getInt("f_id") : last;
                        if (CPref.lastMessage > 0) {
                            switch (mo.getInt("msg")) {
                                case 1:
                                    CDatabase db = new CDatabase(this);
                                    db.exec("delete from body");
                                    db.exec("update tables set header=''");
                                    Intent intent = new Intent("cleardroid");
                                    intent.putExtra("cleardroid", 1);
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                                    break;
                            }
                        }
                    }
                    CPref.setLastMessage(this, last);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

package com.example.pokeapp;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

public class PollTask implements Runnable{
    public static final int INTERVAL = 15000; // time in milliseconds between each activation

    private Handler handler;
    private NotiMan notiMan;
    private FileMan fileMan;

    // TODO change min build version so we don't need these
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public PollTask(Handler handler, Context context) {
        this.handler = handler;
        this.notiMan = new NotiMan(context);
        this.fileMan = new FileMan(context);
    }

    @Override
    public void run() {
        poll();
    }

    private synchronized void poll() {
        Log.d("POLL", "did poll()");
        String uuid = fileMan.getUUID();
        RequestManager.addPollRequest(uuid, response -> {
            // TODO move this away
            try {
                JSONObject resJSON = new JSONObject(response);
                JSONArray pokes = resJSON.getJSONArray("pokes");
                for (int i = 0; i < pokes.length(); i++) {
                    JSONArray poke = pokes.getJSONArray(i);
                    String recvUUID = poke.getString(0);
                    String payload = poke.getString(1);
                    notiMan.createNotification(recvUUID + "\n" + payload);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        handler.postDelayed(this, INTERVAL); // delay handler
    }
}
package com.example.pokeapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.lang.*;

import com.example.pokeapp.Pokey;
import com.example.pokeapp.PokeyMaker;

//Register request
//type in name, ex "wewlad"
//send request to http://zachlef.in/register/name=wewlad, returns UUID

public class MainActivity extends AppCompatActivity {

    NotiMan notificationManager;
    FileMan fileManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sdk version is needed for getSystemService
        notificationManager = new NotiMan(this);
        fileManager = new FileMan(this);
    }

    //Called when QR is pressed
    //switch to qr activity
    public void openQR(View v) {
        Intent i = new Intent(this, qrGenActivity.class);
        startActivity(i);
    }

    //Called when Dummy1 is pressed
    //for networking: switch to register activity
    public void d1(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //Called when Dummy2 is pressed
    //for networking: loads UUID from file
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void d2(View v) {
        TextView text = (TextView)findViewById(R.id.uuidView);
        text.setText(fileManager.getName() + "\n" + fileManager.getUUID());
    }

    /*
    =====NOTIFICATION BRANCH=====
    */

    public void notify(View v) {
        notificationManager.createNotification();
    }

}
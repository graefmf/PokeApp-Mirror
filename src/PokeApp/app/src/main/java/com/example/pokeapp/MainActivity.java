package com.example.pokeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

    public void scan(View v) {
        Intent i = new Intent(this, scanActivity.class);
        startActivity(i);
    }

    //Called when register is pressed
    //for networking: switch to register activity
    public void d1(View v) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    //Called when showUUID is pressed
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
package com.example.pokeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RegisterActivity extends AppCompatActivity {

    RequestQueue queue;
    PokeyMaker p;
    FileMan fileManager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue = Volley.newRequestQueue(this); //request queue
        p = new PokeyMaker(); //basically just a thread factory
        fileManager = new FileMan(this);
    }

    Thread wait;
    String regiResult = null;
    public void sendRegiRequest(View v) {
        String nameText = ((EditText)findViewById(R.id.nameField)).getText().toString();
        final String args[] = {"register", nameText};
        if(nameText != "") {
            //create pokey thread to register
            Thread t = p.newThread(new Pokey(queue, p, "https://poke.zachlef.in/poke/register", args));
            t.start();
            //create thread to wait for result
            wait = new Thread(new Runnable(){
                @Override
                public void run() {
                    while(true) {
                        regiResult = p.getResult();
                        if(regiResult != null) break;
                    }
                    fileManager.writeName(args[1]);
                    fileManager.writeUUID(regiResult);
                    fileManager.updateFile();
                    returnToMain();
                }
            });
            wait.start();
        }
    }

    private void returnToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
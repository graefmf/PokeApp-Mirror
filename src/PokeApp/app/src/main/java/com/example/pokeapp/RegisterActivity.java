package com.example.pokeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    RequestQueue queue;
    PokeyMaker p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        queue = Volley.newRequestQueue(this);
        p = new PokeyMaker();
    }

    Thread wait;
    String regiResult = null;
    public void sendRegiRequest(View v) {
        final String nameText = ((EditText)findViewById(R.id.nameField)).getText().toString();
        if(nameText != "") {
            //create pokey thread to register
            Thread t = p.newThread(new Pokey(queue, "https://poke.zachlef.in/poke/register/name="+nameText, p));
            t.start();
            //create thread to wait for result
            wait = new Thread(new Runnable(){
                @Override
                public void run() {
                    while(true) {
                        regiResult = p.getResult();
                        if(regiResult != null) break;
                    }
                    saveUUID(nameText + '\n' + regiResult);
                }
            });
            wait.start();
        }
    }

    public void saveUUID(String U) {
        String filename = getString(R.string.uuid_file);
        String contents = U;
        FileOutputStream fos = null;
        //open file output and write string as bytes
        try {
            fos = this.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(contents.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //close file, if it was ever opened
        if(fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //return to main activity
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
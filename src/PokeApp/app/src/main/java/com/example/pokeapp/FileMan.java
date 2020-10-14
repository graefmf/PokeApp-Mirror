package com.example.pokeapp;

import android.content.Context;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class FileMan {

    private Context from;
    private JSONObject data;

    //constructor
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public FileMan(Context from) {
        this.from = from;
        data = new JSONObject();
        readData();
    }

    //public function to write name
    public void writeName(String n) {
        try {
            //writes name to program data (NOT FILE).
            //will replace an existing value automatically
            data.put("name",n);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //public function to write UUID
    public void writeUUID(String u) {
        try {
            //writes UUID to program data (NOT FILE).
            //will replace an existing value automatically
            data.put("UUID",u);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Gets name from program data. Returns empty if nothing stored.
    public String getName() {
        String n = "";
        try {
            n = data.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return n;
    }

    //Gets UUID from program data. Returns empty if nothing stored.
    public String getUUID() {
        String n = "";
        try {
            n = data.getString("UUID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return n;
    }

    public void updateFile() {
        writeData();
    }

    //writes program data to file
    private void writeData() {
        String filename = from.getString(R.string.uuid_file);
        String contents = data.toString(); //raw JSON string
        FileOutputStream fos = null;
        //open file output and write string as bytes
        try {
            fos = from.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(contents.getBytes());
            fos.close();
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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void readData() {
        String filename = from.getString(R.string.uuid_file);
        FileInputStream fis = null;
        InputStreamReader isr = null;
        //open file input to get back earlier thing
        try {
            fis = from.openFileInput(filename);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //assuming that worked, build string out of file contents
        if(fis != null && isr != null) {
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(isr)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }
                String result = stringBuilder.toString();
                //Log.d("FILE", result);
                //create a new JSON object from the raw string obtained from file
                data = new JSONObject(result);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

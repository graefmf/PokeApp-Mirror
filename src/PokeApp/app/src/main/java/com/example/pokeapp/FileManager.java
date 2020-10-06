package com.example.pokeapp;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileManager {

    public static void writePrivate(Activity activity, String filename, String data) {
        String contents = data;
        FileOutputStream fos = null;
        //open file output and write string as bytes
        try {
            fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);
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
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String readPrivate(Activity activity, String filename) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        //open file input to get back earlier thing
        try {
            fis = activity.openFileInput(filename);
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
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return stringBuilder.toString();
            }
        }
        else {
            return null;
        }
    }
}

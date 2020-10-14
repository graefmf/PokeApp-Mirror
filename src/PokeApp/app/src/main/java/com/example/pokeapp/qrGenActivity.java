package com.example.pokeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class qrGenActivity extends AppCompatActivity{

    FileMan fileManager;
    private Bitmap loadedBmp = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_gen);
        //finds image view and sets image to generated bitmap
        ImageView qrView = (ImageView) findViewById(R.id.qrView);

        fileManager = new FileMan(this);
        loadedBmp = createQrCodeFromUUID(fileManager.getUUID());
        //If no valid UUID exists, display no valid UUID
        TextView noValidUUID = (TextView) findViewById(R.id.noQrCode);
        TextView uuidText = (TextView) findViewById(R.id.uuidViewQR);
        //gets UUID from fileman
        uuidText.setText(fileManager.getName() + "\n" + fileManager.getUUID());
        if(loadedBmp == null) {
            noValidUUID.setText("No Valid UUID Loaded");
        }
        qrView.setImageBitmap(loadedBmp);

    }

    //new qr code creation using zxing
    private Bitmap createQrCodeFromUUID(String UUID){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(UUID, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void leaveQR(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
package com.csc317.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DisplayFragment weatherDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherDisplay = new DisplayFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.weather_layout_container,weatherDisplay);
        transaction.addToBackStack(null);
        transaction.commit();
        setContentView(R.layout.activity_main);
    }

    /**
     * take a screenshot of current display, share with contacts by email
     */
    public void shareButtonOnClick(View view) {
        //Step1: get the root view, save it as a Bitmap
        View screenView = getWindow().getDecorView().getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);

        //Step2: create a file in external storage and get the path
        File screenshot = null;
        String absolutePath = "";
        try {
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String imageFileName = "WeatherForecast_" + timeStamp;  //example: WeatherForecast_2020-11-29.jpg
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            screenshot = File.createTempFile(imageFileName, ".png", storageDir);
            absolutePath = screenshot.getAbsolutePath();
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!absolute path:"+absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Step3: write the Bitmap to the external file by the path get from step2, get the URI
        try (FileOutputStream out = new FileOutputStream(absolutePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri screenshotUri = FileProvider.getUriForFile(this, "com.csc317.weatherapp.fileprovider", screenshot);

        //Step4: Use Implicit intent to share the screenshot
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        // The uri should be the uri of the screenshot image
        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/png");
        startActivity(intent);
    }
}
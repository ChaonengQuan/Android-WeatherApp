package com.csc317.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DisplayFragment weatherDisplay;
    private ContactSelectFragment contactSelectFragment;
    private Uri screenshotUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherDisplay = new DisplayFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.weather_layout_container,weatherDisplay);
        transaction.addToBackStack(null);
        transaction.commit();
        setContentView(R.layout.activity_main);

//        //test geocoder
//        Geocoder geocoder = new Geocoder(this);
//
//        try {
//
//            List<Address> addressList = geocoder.getFromLocationName("Tucson", 3);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            System.out.println(addressList.get(0));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


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
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!absolute path:"+absolutePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Step3: write the Bitmap to the external file by the path get from step2, get the URI
        try (FileOutputStream out = new FileOutputStream(absolutePath)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        screenshotUri = FileProvider.getUriForFile(this, "com.csc317.weatherapp.fileprovider", screenshot);

        //Step4: Create a Contact List View fragment, passing in the uri
        contactSelectFragment = ContactSelectFragment.newInstance(screenshotUri.toString());
        //Replace the layout in main with this contactFragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.weather_layout_container, contactSelectFragment);
        transaction.addToBackStack(null);   //can return to drawing
        transaction.commit();
    }
}
package com.csc317.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.TelephonyNetworkSpecifier;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DisplayFragment weatherDisplay;
    private ContactSelectFragment contactSelectFragment;
    private Uri screenshotUri = null;
    private String latitude = "32.2226";
    private String longitude = "-110.9747";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherDisplay = new DisplayFragment();
        weatherDisplay.setLatitude(latitude);
        weatherDisplay.setLongitude(longitude);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.weather_layout_container,weatherDisplay);
        transaction.addToBackStack(null);
        transaction.commit();
        setContentView(R.layout.activity_main);
    }


    /**
     * Update weather info based the city name user entered
     */
    public void searchWeatherByCity(View view) {
        //Step1: Update the TextView city label
        EditText editText = findViewById(R.id.edit_text);
        String cityNameEntered = editText.getText().toString();
        TextView cityLabel = findViewById(R.id.city_label);
        cityLabel.setText(cityNameEntered);

        //Step2: Get the longitude and latitude using Geocoder class
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(cityNameEntered, 3);
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            Address targetAddress = addressList.get(0);
            latitude = String.format(Locale.US, "%.4f", targetAddress.getLatitude());
            longitude = String.format(Locale.US, "%.4f", targetAddress.getLongitude());
            weatherDisplay.setLatitude(latitude);
            weatherDisplay.setLongitude(longitude);
            weatherDisplay.updateWeather();
//            System.out.println(latitude);
//            System.out.println(longitude);
//            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
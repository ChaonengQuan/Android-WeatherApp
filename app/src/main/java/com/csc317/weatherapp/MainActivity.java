package com.csc317.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

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
}
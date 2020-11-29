package com.csc317.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DisplayFragment extends Fragment {
    private Bitmap icon;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.display_fragment, container, false);
    }

    private class WeatherDownloadIcon extends AsyncTask<URL, Integer, Long> {

        /**
         * doInBackground
         * @param urls The icon URL returned by the API call
         * Uses HttpURLConnection to download image bitmap of the weather icon
         * @return 0
         */
        @Override
        protected Long doInBackground(URL... urls) {
            try {
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                icon = BitmapFactory.decodeStream(input);
                return new Long(0);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * onPostExecute
         * @param result
         * As long as valid bitmap was retrieved, set the weather icon to that image.
         * If no bitmap is retrieved, use the default sun image.
         */
        protected void onPostExecute(Long result) {
            ImageView iconView = (ImageView) getView().findViewById(R.id.weather_icon);
            if(icon != null) {
                iconView.setImageBitmap(icon);
            } else {
                iconView.setImageBitmap(BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),R.drawable.sun));
            }
        }
    }
}

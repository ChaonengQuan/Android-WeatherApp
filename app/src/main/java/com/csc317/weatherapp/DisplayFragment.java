package com.csc317.weatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class DisplayFragment extends Fragment {
    private Bitmap icon;

    Integer[] nameIdArray = new Integer[]{R.id.day_1_name, R.id.day_2_name, R.id.day_3_name, R.id.day_4_name, R.id.day_5_name, R.id.day_6_name};
    Integer[] tempIdArray = new Integer[]{R.id.day_1_temp, R.id.day_2_temp, R.id.day_3_temp, R.id.day_4_temp, R.id.day_5_temp, R.id.day_6_temp};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WeatherForecastAsyncTask webRequest = new WeatherForecastAsyncTask();
        webRequest.execute();

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

    /**
     * Private Inner Class
     * Make web request based on the office location and gridX/Y coordinates parameters
     * Parse URL request result into a JSONObject.
     *
     * Parameters:
     *          params[0]   -   the office code (e.g. TWC)
     *          params[1]   -   the gridX and gridY (e.g. 91,48)
     */
    private class WeatherForecastAsyncTask extends AsyncTask<String, Void, JSONObject> {
        /**
         * Make web request to the National Weather Service(NWS) API
         * Store the result in a jsonObject
         * @param params - the target coordinates of the city
         * @return - a JSON object fetched from the API
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            try {
                StringBuilder json = new StringBuilder();
                String line;
                URL url = new URL("https://api.weather.gov/gridpoints/TWC/91,48/forecast"); //Hard code params for Tucson
                URLConnection urlConn = url.openConnection();
                urlConn.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.284");
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                while ((line = in.readLine()) != null) {
                    //System.out.println("JSON LINE " + line);
                    json.append(line);
                }
                in.close();
                jsonObject = new JSONObject(json.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        /**
         * Update UI based on the parsed jsonObject
         * @param jsonObject - a jsonObject passed from doInBackground()
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                JSONArray weeklyJSONArray = jsonObject.getJSONObject("properties").getJSONArray("periods");
                //System.out.println(weeklyJSONArray);

                //today's weather
                JSONObject todayJSONObject = (JSONObject) weeklyJSONArray.get(0);
                new WeatherDownloadIcon().execute(new URL(todayJSONObject.getString("icon")));
                TextView todayTemperature = getActivity().findViewById(R.id.temperature);
                todayTemperature.setText(todayJSONObject.getString("temperature") + "\u2109");

                /*update weekly weather*/
                //days after 'today'
                for(int i = 0; i < nameIdArray.length; i++){
                    TextView name = getActivity().findViewById(nameIdArray[i]);
                    TextView temp =  getActivity().findViewById(tempIdArray[i]);
                    JSONObject day1JSONObject = (JSONObject) weeklyJSONArray.get((i+1)*2);
                    name.setText(day1JSONObject.getString("name"));
                    temp.setText(day1JSONObject.getString("temperature") + "\u2109");
                }

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

        }

    }
}
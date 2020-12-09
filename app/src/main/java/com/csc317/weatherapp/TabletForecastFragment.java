package com.csc317.weatherapp;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class TabletForecastFragment extends Fragment {

    private String dayName;
    private String latitude;        //default geo location for Tucson
    private String longitude;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        new WeatherForecastAsyncTask().execute();   //update fragment UI

        return inflater.inflate(R.layout.detailed_forecast, container, false);
    }

    public void setDayName(String dayName) { this.dayName = dayName; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }



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
         *
         * @param params - the target coordinates of the city
         * @return - a JSON object fetched from the API
         */
        @Override
        protected JSONObject doInBackground(String... params) {
            //System.out.println("!!!!!!I am in the AsyncTask!!!!!!");

            JSONObject jsonObject = null;
            try {
                /*Make 1st API call to get the GridID, GridX, and GridY*/
                StringBuilder jsonFirst = new StringBuilder();
                String lineFirst;
                URL urlFirst = new URL("https://api.weather.gov/points/" + latitude + "," + longitude); //Hard code params for Tucson
                URLConnection urlConnFirst = urlFirst.openConnection();
                urlConnFirst.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.284");
                BufferedReader inFirst = new BufferedReader(new InputStreamReader(urlConnFirst.getInputStream()));
                while ((lineFirst = inFirst.readLine()) != null) {
                    //System.out.println("JSON LINE " + line);
                    jsonFirst.append(lineFirst);
                }
                inFirst.close();
                jsonObject = new JSONObject(jsonFirst.toString());

                JSONObject properties = jsonObject.getJSONObject("properties");
                String gridID = properties.getString("gridId");
                String gridX = properties.getString("gridX");
                String gridY = properties.getString("gridY");

                //System.out.println(gridID + ", " + gridX + "," + gridY);

                /*Make the 2nd API call to get the weather information*/
                StringBuilder json = new StringBuilder();
                String line;
                URL url = new URL("https://api.weather.gov/gridpoints/"+gridID+"/"+gridX+","+gridY+"/forecast"); //Hard code params for Tucson
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
         *
         * @param jsonObject - a jsonObject passed from doInBackground()
         */
        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                JSONArray weeklyJSONArray = jsonObject.getJSONObject("properties").getJSONArray("periods");
                //System.out.println(weeklyJSONArray);

                for (int i=0; i < weeklyJSONArray.length(); i++) {
                    JSONObject dayJsonObject = weeklyJSONArray.getJSONObject(i);
                    //Populate TextView
                    if(dayJsonObject.getString("name").equals(dayName)){
                        TextView dayLabel = getActivity().findViewById(R.id.day_label);
                        TextView shortForecastContent = getActivity().findViewById(R.id.short_forecast_content);
                        TextView detailedForecastContent = getActivity().findViewById(R.id.detailed_forecast_content);

                        dayLabel.setText(dayName);
                        shortForecastContent.setText(dayJsonObject.getString("shortForecast"));
                        detailedForecastContent.setText(dayJsonObject.getString("detailedForecast"));

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}

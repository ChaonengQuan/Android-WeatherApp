package com.csc317.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DisplayFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        WeatherForecastAsyncTask webRequest = new WeatherForecastAsyncTask();
        webRequest.execute();

        return inflater.inflate(R.layout.display_fragment, container, false);
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

                /*update weekly weather*/

                //today's weather
                JSONObject todayJSONObject = (JSONObject) weeklyJSONArray.get(0);
                TextView todayTemperature = getActivity().findViewById(R.id.temperature);
                todayTemperature.setText(todayJSONObject.getString("temperature"));

                //the day after 'today'
                TextView day_1_name = getActivity().findViewById(R.id.day_1_name);
                TextView day_1_temp =  getActivity().findViewById(R.id.day_1_temp);
                JSONObject day1JSONObject = (JSONObject) weeklyJSONArray.get(2);
                day_1_name.setText(day1JSONObject.getString("name"));
                day_1_temp.setText(day1JSONObject.getString("temperature"));

                TextView day_2_name = getActivity().findViewById(R.id.day_2_name);
                TextView day_2_temp =  getActivity().findViewById(R.id.day_2_temp);
                JSONObject day2JSONObject = (JSONObject) weeklyJSONArray.get(4);
                day_2_name.setText(day2JSONObject.getString("name"));
                day_2_temp.setText(day2JSONObject.getString("temperature"));

                TextView day_3_name = getActivity().findViewById(R.id.day_3_name);
                TextView day_3_temp =  getActivity().findViewById(R.id.day_3_temp);
                JSONObject day3JSONObject = (JSONObject) weeklyJSONArray.get(6);
                day_3_name.setText(day3JSONObject.getString("name"));
                day_3_temp.setText(day3JSONObject.getString("temperature"));

                TextView day_4_name = getActivity().findViewById(R.id.day_4_name);
                TextView day_4_temp =  getActivity().findViewById(R.id.day_4_temp);
                JSONObject day4JSONObject = (JSONObject) weeklyJSONArray.get(8);
                day_4_name.setText(day4JSONObject.getString("name"));
                day_4_temp.setText(day4JSONObject.getString("temperature"));

                TextView day_5_name = getActivity().findViewById(R.id.day_5_name);
                TextView day_5_temp =  getActivity().findViewById(R.id.day_5_temp);
                JSONObject day5JSONObject = (JSONObject) weeklyJSONArray.get(10);
                day_5_name.setText(day5JSONObject.getString("name"));
                day_5_temp.setText(day5JSONObject.getString("temperature"));

                TextView day_6_name = getActivity().findViewById(R.id.day_6_name);
                TextView day_6_temp =  getActivity().findViewById(R.id.day_6_temp);
                JSONObject day6JSONObject = (JSONObject) weeklyJSONArray.get(12);
                day_6_name.setText(day6JSONObject.getString("name"));
                day_6_temp.setText(day6JSONObject.getString("temperature"));

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }
}

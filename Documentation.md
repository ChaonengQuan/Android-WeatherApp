# Weather App

## Overview
Weather App is a Java Android mobile application that enables the user to browse weather forecast information based on the city entered. It supports both phone and tablet with different layout on both devices. 

Weather forecast information is retrieved from [National Weather Service (NWS)](https://www.weather.gov/documentation/services-web-api) API.

Team members: Thomas Ruff, Chaoneng Quan

## User Interface
1. UI layout

    The initial screen shown to the user will consist of a large display of the temperature and weather for the chosen city. The default is loaded from the shared preferences. Below this main information, we can display additional forecasts for the rest of the week. Various sun/moon/cloud icons will be used for displaying the weather on each day. The main screen will then have a "Get weather" button to display the forecast for the user's current location. Finally on the main screen will be a "Share" button to allow the user to email a picture of the forecast to one of their contacts.

2. Assets
    
    The background of the forecast will make use of rain, sun, snow, or other applicable animations depending on the current weather. Icons will be used from Icons8 for the app.

3. Prototype UI

![UI Preview](https://github.com/tnruff/csc317-weatherapp/blob/dev/uipreview.PNG)

## Technical Details
1. Fragment
    This app will user Fragment in order to support both phone and tablet device. On a small screen size device it will only display one Fragment at a time. On a large screen size device it can display two Fragments on same screen.

    1. Weather Fragment
    2. Search Fragment

2. Content Provider

    This app wil use the contacts content provider in Android to display the list of contacts, then allows user to click on the contact to share with. After user clicked on the contact to share, it will then get the email address for a given content in order to setup the intent to send an email.

3. Implicit Intent
    
    This app will only support sharing via email, rather than any messaging app in general. It will also support sending an image to a specific email (the email of the desired contact), rather than just a general email.

    In another word, once user clicked on the share button and chosen the contact to share with. It will pre-fill the email body with a screenshot of the current weather, and pre-fill the destination address with chosen contact's email address.

4. Weather API Web Service

    This app will use the National Weather Service (NWS) API to retrieve weekly weather forecast information. 
    
    NWS API divide US map into 2.5km grids, and each grid is label with x and y index. To use this API we need to provide three parameters: office, gridX, gridY.

    This app will first use LocationManager object in Android to get current latitude and longitude, then use latitude and longitude to get the office, gridX, gridY information from the API, then finally use office, gridX, gridY to get weekly weather forecast information.

    Example Usage:
    ```
    https://api.weather.gov/gridpoints/{office}/{gridX},{gridY}/forecast
    ```

    Snippet of JSON data retrieved:
    ```
    {
        "number": 3,
        "name": "Monday",
        "startTime": "2020-11-16T06:00:00-06:00",
        "endTime": "2020-11-16T18:00:00-06:00",
        "isDaytime": true,
        "temperature": 63,
        "temperatureUnit": "F",
        "temperatureTrend": null,
        "windSpeed": "5 to 15 mph",
        "windDirection": "NW",
        "icon": "https://api.weather.gov/icons/land/day/few?size=medium",
        "shortForecast": "Sunny",
        "detailedForecast": "Sunny, with a high near 63. Northwest wind 5 to 15 mph."
    },
    ```
    
5. Animation

    Different animation will be displayed based on the current weather of the day.
    - Sunny: sun on the screen
    - Rain: rain drops on the screen
    - Windy: moving leaves on the screen
    - Snow: snow flakes on the screen

6. Project Structure

    ```bash
    ├── MainActivity
    ├── WeatherFragment
    │   └──  AsyncTask
    ├── SearchFragment
    │   └──  AsyncTask
    ├── ContentProvider
    └── WeatherInfo
    ```

    - MainActivity

        Setup Fragments and entry point of each button's onClick functions.

    - ContentProvider

        Retrieve contact email and send Implicit Intent
    
    - WeatherInfo
        
        A object that stores the weather forecast information, including day, temperature, temperatureUnit, windSpeed, weatherIconURL, shortForecast, detailedForecast


## Timeline
- Project Document - 11/16/2020
- Milestone1 - 11/23/2020
    
    - Main UI, Web API, Content Provider should be done
    - Estimated work hours: 5 hours

- Checkpoint Meeting - 11/30/2020 
    
    - Finish Minimal viable product should be done
    - Estimated work hours: 5 hours

- Project presentation - 12/7/2020 and 12/9/2020

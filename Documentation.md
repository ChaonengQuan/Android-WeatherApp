# Weather App

## Overview
Weather App is a Java Android mobile application that enables the user to browse weather forecast information based on the city entered. 

Team member: Chaoneng Quan, Thomas Ruff

## User Interface
1. UI layout

    The initial screen shown to the user will consist of a large display of the temperature and weather for the chosen city. The default is loaded from the shared preferences. Below this main information, we can display additional forecasts for the rest of the week. Various sun/moon/cloud icons will be used for displaying the weather on each day. The main screen will then have a "Get weather" button to display the forecast for the user's current location. Finally on the main screen will be a "Share" button to allow the user to email a picture of the forecast to one of their contacts.

2. Assets
    
    The background of the forecast will make use of rain, sun, snow, or other applicable animations depending on the current weather. Icons will be used from icons8 for the app.

3. Prototype UI

## Technical Details
1. Fragment
2. Implicit Intent
3. Content Provider
4. Weather API Web Service

    This app will use [The National Weather Service (NWS) API](https://www.weather.gov/documentation/services-web-api) to retrieve weekly weather forecast information. 
    
    NWS API divide US map into 2.5km grids, and each grid is label with x and y index. To use this API we need to provide three parameters: office, gridX, gridY.

    This app will first use LocationManager object in Android to get current latitude and longitude, then use latitude and longitude to get the office, gridX, gridY information from the API, then finally use office, gridX, gridY to get weekly weather forecast information.

    Example Usage:
    ```
    https://api.weather.gov/gridpoints/{office}/{grid X},{grid Y}/forecast
    ```
    
5. Animation

## Timeline
- Project Document - 11/16/2020 
- Checkpoint Meeting - 11/30/2020 
- Project presentation - 12/7/2020 and 12/9/2020
- Project - 12/9/2020 

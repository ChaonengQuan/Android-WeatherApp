# Weather App

## Overview
Weather App is a Java Android mobile application that enables the user to browse weather forecast information based on the city entered. 

Team member: Chaoneng Quan, Thomas Ruff

## User Interface
1. UI layout
2. xxx
3. etc

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
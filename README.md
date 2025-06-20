# Halifax Bus App

This is an Android app that displays real-time bus locations in Halifax using the GTFS (General Transit Feed Specification) realtime feeds. The app shows bus positions on a Mapbox map and updates every 15 seconds.

## Features

* Displays live bus positions on a Mapbox map.
* Shows bus routes with labels.
* Uses location permissions to show user location on the map.
* Automatically refreshes bus data every 15 seconds.

## Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Make sure you have a Mapbox access token set up in your project.
4. Build and run on a device or emulator with internet access.
5. Grant location permissions when prompted.

## Technologies Used

* Kotlin
* Jetpack Compose
* Mapbox Maps SDK for Android
* Google Accompanist Permissions
* GTFS Realtime Protocol Buffers
* Coroutines and Flow

## Notes

* The app fetches GTFS realtime data from [Halifax Transit’s public feeds](https://gtfs.halifax.ca/).
* The bus icon image should be placed in the `drawable` resource folder.


package com.example.halifaxbusapps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.transit.realtime.GtfsRealtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URL

class MainViewModel : ViewModel() {

    // Holds GTFS vehicle position feed
    private val _gtfs = MutableStateFlow<GtfsRealtime.FeedMessage?>(null)
    val gtfs = _gtfs.asStateFlow()

    // Holds GTFS trip updates (for upcoming buses)
    private val _tripUpdates = MutableStateFlow<List<GtfsRealtime.TripUpdate>>(emptyList())
    val tripUpdates = _tripUpdates.asStateFlow()

    // Fetch real-time vehicle positions from Halifax GTFS feed
    fun loadBusPositions() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val feedUrl = "https://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb"
                val input = URL(feedUrl).openStream()
                val feed = GtfsRealtime.FeedMessage.parseFrom(input)
                _gtfs.value = feed
            } catch (e: Exception) {
                Log.e("GTFS", "Error loading vehicle positions: ${e.message}")
            }
        }
    }


    fun loadTripUpdates() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL("https://gtfs.halifax.ca/realtime/Trip/TripUpdates.pb")
                val feed = GtfsRealtime.FeedMessage.parseFrom(url.openStream())
                val updates = feed.entityList.mapNotNull { it.tripUpdate }
                _tripUpdates.value = updates
            } catch (e: Exception) {
                Log.e("GTFS", "Error loading trip updates: ${e.message}")
            }
        }
    }
}

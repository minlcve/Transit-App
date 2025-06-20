package com.example.halifaxbusapps.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.halifaxbusapps.viewmodel.MainViewModel
import com.google.transit.realtime.GtfsRealtime

@Composable
fun OtherScreen(viewModel: MainViewModel) {
    val tripUpdates by viewModel.tripUpdates.collectAsState()

    // Load trip updates when the screen is first shown
    LaunchedEffect(Unit) {
        viewModel.loadTripUpdates()
    }

    // If no data from the API, show mock data as fallback
    val displayTrips = if (tripUpdates.isEmpty()) {
        listOf(
            GtfsRealtime.TripUpdate.newBuilder()
                .setTrip(GtfsRealtime.TripDescriptor.newBuilder().setRouteId("1"))
                .addStopTimeUpdate(
                    GtfsRealtime.TripUpdate.StopTimeUpdate.newBuilder()
                        .setStopId("Duke & Barrington")
                        .setDeparture(
                            GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder().setDelay(300) // 5 min
                        )
                ).build(),
            GtfsRealtime.TripUpdate.newBuilder()
                .setTrip(GtfsRealtime.TripDescriptor.newBuilder().setRouteId("10"))
                .addStopTimeUpdate(
                    GtfsRealtime.TripUpdate.StopTimeUpdate.newBuilder()
                        .setStopId("Spring Garden & Queen")
                        .setDeparture(
                            GtfsRealtime.TripUpdate.StopTimeEvent.newBuilder().setDelay(600) // 10 min
                        )
                ).build()
        )
    } else {
        tripUpdates
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Upcoming Buses", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        if (displayTrips.isEmpty()) {
            Text("No upcoming buses available.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(displayTrips) { trip ->
                    TripCard(trip)
                }
            }
        }
    }
}

@Composable
fun TripCard(trip: GtfsRealtime.TripUpdate) {
    val stopUpdate = trip.stopTimeUpdateList.firstOrNull()
    val stopId = stopUpdate?.stopId ?: "Unknown Stop"
    val delaySec = stopUpdate?.departure?.delay ?: 0
    val minutes = delaySec / 60

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Route ${trip.trip.routeId}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Stop: $stopId")
            Text("Next bus in $minutes min")
        }
    }
}

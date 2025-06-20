package com.example.halifaxbusapps.screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toBitmap
import com.example.halifaxbusapps.R
import com.example.halifaxbusapps.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.coroutines.delay


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {
    val permissionsState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )

    // Request location permissions on launch
    LaunchedEffect(Unit) {
        if (!permissionsState.allPermissionsGranted) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    val context = LocalContext.current
    val gtfsFeed by viewModel.gtfs.collectAsState()

    val downtownHalifax = Point.fromLngLat(-63.572389, 44.647529)

    // Refresh vehicle positions every 15 seconds
    LaunchedEffect("auto-refresh") {
        while (true) {
            viewModel.loadBusPositions()
            delay(15_000)
        }
    }

    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx, MapInitOptions(ctx))

            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->

                // Set initial camera position
                mapView.getMapboxMap().setCamera(
                    CameraOptions.Builder()
                        .center(downtownHalifax)
                        .zoom(15.5)
                        .build()
                )

                // Enable location if permission is granted
                if (permissionsState.allPermissionsGranted) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }
                }

                // Load bus icon from drawable
                val busDrawable = context.getDrawable(R.drawable.bus)
                if (busDrawable == null) {
                    Log.e("MapScreen", "bus.png not found in drawable folder")
                    return@loadStyleUri
                }
                val imageId = "bus-icon"
                style.addImage(imageId, busDrawable.toBitmap())

                val annotationPlugin = mapView.annotations
                val pointAnnotationManager = annotationPlugin.createPointAnnotationManager(
                    AnnotationConfig("bus-layer")
                )

                // Clear existing markers
                pointAnnotationManager.deleteAll()

                val buses = gtfsFeed?.entityList.orEmpty()
                Log.d("GTFS", "Loaded ${buses.size} buses")

                // Add a marker for each bus
                buses.forEach { entity ->
                    val vehicle = entity.vehicle
                    if (vehicle.hasPosition()) {
                        val lat = vehicle.position.latitude
                        val lon = vehicle.position.longitude
                        val route = vehicle.trip.routeId ?: "?"

                        val point = Point.fromLngLat(lon.toDouble(), lat.toDouble())

                        val options = PointAnnotationOptions()
                            .withPoint(point)
                            .withIconImage(imageId)
                            .withTextField(route)
                            .withTextSize(14.0)
                            .withTextOffset(listOf(0.0, -0.35))
                            .withTextColor("#003366")
                            .withTextHaloColor("#FFFFFF")
                            .withTextHaloWidth(2.5)

                        pointAnnotationManager.create(options)
                        Log.d("MapScreen", "Bus added at lat:$lat lon:$lon route:$route")
                    }
                }
            }

            mapView
        },
        modifier = Modifier
    )
}

package com.example.halifaxbusapps.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.halifaxbusapps.screens.MapScreen
import com.example.halifaxbusapps.screens.OtherScreen
import com.example.halifaxbusapps.viewmodel.MainViewModel

@Composable
fun NavGraph(viewModel: MainViewModel) {
    // Sets up the navigation controller to handle navigation
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // Bottom navigation bar with two tabs: Map and Alerts
            NavigationBar {
                NavigationBarItem(
                    selected = navController.currentBackStackEntry?.destination?.route == "map",
                    onClick  = { navController.navigate("map") },
                    icon     = { Icon(Icons.Filled.Map, contentDescription = "Map") },
                    label    = { Text("Map") }
                )
                NavigationBarItem(
                    selected = navController.currentBackStackEntry?.destination?.route == "other",
                    onClick  = { navController.navigate("other") },
                    icon     = { Icon(Icons.Filled.Info, contentDescription = "Alerts") },
                    label    = { Text("Alerts") }
                )
            }
        }
    ) { innerPadding ->
        // Main navigation container that swaps screens based on routes
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Route for the map screen
            composable("map") {
                MapScreen(viewModel)
            }

            // Route for the alerts/other screen
            composable("other") {
                OtherScreen(viewModel)
            }
        }
    }
}

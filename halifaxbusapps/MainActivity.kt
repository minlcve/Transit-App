package com.example.halifaxbusapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.halifaxbusapps.nav.NavGraph
import com.example.halifaxbusapps.ui.theme.HalifaxBusAppTheme
import com.example.halifaxbusapps.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Apply app theme and set up navigation graph with ViewModel
            HalifaxBusAppTheme {
                val viewModel: MainViewModel = viewModel()
                NavGraph(viewModel = viewModel)
            }
        }
    }
}

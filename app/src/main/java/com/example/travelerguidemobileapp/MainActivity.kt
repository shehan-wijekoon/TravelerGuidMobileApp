package com.example.travelerguidemobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelerguidemobileapp.controllers.Screen
import com.example.travelerguidemobileapp.screens.NewDestinationScreen
import com.example.travelerguidemobileapp.ui.theme.TravelerGuideMobileAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelerGuideMobileAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.NewDestination.route
    ) {
        // Route for the New Destination Screen
        composable(route = Screen.NewDestination.route) {
            NewDestinationScreen(navController = navController)
        }

        // You would add other screens here, like the Home Screen
        composable(route = Screen.Home.route) {
            // HomeScreen(...) // Placeholder
        }
    }
}

// Optional: Preview the navigation setup
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TravelerGuideMobileAppTheme {
        AppNavigation()
    }
}
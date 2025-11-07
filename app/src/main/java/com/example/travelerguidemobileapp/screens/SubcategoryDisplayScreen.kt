package com.example.travelerguidemobileapp.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerguidemobileapp.data.util.DummyDestination
import com.example.travelerguidemobileapp.data.util.dummyDestinations
import com.example.travelerguidemobileapp.controllers.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcategoryDisplayScreen(
    navController: NavController,
    subcategoryId: String,
    subcategoryName: String
) {
    // 🎯 LOCAL DATA FILTERING (Simulating ViewModel filter)
    val destinations = remember(subcategoryId) {
        dummyDestinations.filter { it.subcategoryId == subcategoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(subcategoryName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            if (destinations.isEmpty()) {
                Text("No destinations found in $subcategoryName.", modifier = Modifier.padding(16.dp))
            }

            LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(destinations) { destination ->
                    DestinationCard(destination = destination)
                }
            }
        }
    }
}


// --- Reusable Composable for a single Destination Item ---
@Composable
fun DestinationCard(destination: DummyDestination) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Cover Image
            Image(
                painter = rememberAsyncImagePainter(destination.coverImageUrl),
                contentDescription = "Cover image for ${destination.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))

            // Name and Description
            Text(destination.name, style = MaterialTheme.typography.titleLarge)
            Text(destination.description, style = MaterialTheme.typography.bodyMedium)

            // 🎯 MAP BUTTON
            OpenInMapsButton(mapUrl = destination.googleMapsUrl)
        }
    }
}

// --- Function to launch Maps via Intent ---
@Composable
fun OpenInMapsButton(mapUrl: String) {
    val context = LocalContext.current

    if (mapUrl.isNotBlank() && mapUrl.startsWith("http")) {

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                intent.setPackage("com.google.android.apps.maps") // Attempt to use Maps app first

                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Fallback to regular browser if Maps app fails
                    intent.setPackage(null)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Icon(Icons.Filled.LocationOn, contentDescription = "View Location")
            Spacer(Modifier.width(8.dp))
            Text("View Location on Map")
        }
    }
}
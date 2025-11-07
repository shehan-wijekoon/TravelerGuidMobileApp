package com.example.travelerguidemobileapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.travelerguidemobileapp.controllers.Screen
// Imports for DummyData from util package (assuming you created it in data/util/)
import com.example.travelerguidemobileapp.data.util.DummyCategory
import com.example.travelerguidemobileapp.data.util.dummyCategories

// Helper map for distinct placeholder images (defined in previous steps)
val categoryImages = mapOf(
    "Jungle" to "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?cs=srgb&dl=pexels-vince-2265876.jpg&fm=jpg",
    "Beach" to "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?cs=srgb&dl=pexels-vince-2265876.jpg&fm=jpg",
    "Mountain" to "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?cs=srgb&dl=pexels-vince-2265876.jpg&fm=jpg",
    "City Break" to "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?cs=srgb&dl=pexels-vince-2265876.jpg&fm=jpg",
    "Desert" to "https://images.pexels.com/photos/2265876/pexels-photo-2265876.jpeg?cs=srgb&dl=pexels-vince-2265876.jpg&fm=jpg"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCategoryDisplayScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Traveler Guide Categories") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.NewDestination.route)
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Destination")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Explore Destinations by Category:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            items(dummyCategories) { category ->
                MainCategoryCard( // Calling the separate composable below
                    category = category,
                    onClick = {
                        val route = Screen.SubcategoryList.createSubcategoryListRoute(
                            catId = category.id,
                            catName = category.name
                        )
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}

// --- **MUST BE INCLUDED** to resolve the "Unresolved reference" error ---
@Composable
fun MainCategoryCard(category: DummyCategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = rememberAsyncImagePainter(categoryImages[category.name] ?: "https://picsum.photos/600/300"),
                contentDescription = "Image for ${category.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Category Name Overlay
            Text(
                text = category.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}
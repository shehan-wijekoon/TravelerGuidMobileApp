package com.example.travelerguidemobileapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.travelerguidemobileapp.controllers.Screen
// 🎯 IMPORT Data Classes and Lists from the centralized location
import com.example.travelerguidemobileapp.data.util.DummySubcategory
import com.example.travelerguidemobileapp.data.util.dummySubcategoriesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcategoryListScreen(
    navController: NavController,
    mainCategoryId: String,
    mainCategoryName: String
) {
    // Filter the imported dummy list based on the main category ID
    val filteredSubcategories = remember(mainCategoryId) {
        dummySubcategoriesList.filter { it.parentId == mainCategoryId }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mainCategoryName) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.NewDestination.route) }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    "Select a subcategory in $mainCategoryName:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(filteredSubcategories) { subcategory ->
                SubcategoryListItem(
                    subcategory = subcategory,
                    onClick = {
                        val route = Screen.SubcategoryDisplay.createSubcategoryDisplayRoute(
                            subId = subcategory.id,
                            subName = subcategory.name
                        )
                        navController.navigate(route)
                    }
                )
            }
        }
    }
}

// --- Reusable Item Composable ---
@Composable
fun SubcategoryListItem(subcategory: DummySubcategory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = subcategory.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "View")
        }
    }
}
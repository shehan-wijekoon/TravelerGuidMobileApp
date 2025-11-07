package com.example.travelguidemobileapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.travelguidemobileapp.viewmodel.CreatePostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(viewModel: CreatePostViewModel = viewModel()) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Post") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image URL Input and Display
            Text(
                text = "Add Photos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = viewModel.currentUrlInput.value,
                    onValueChange = { viewModel.currentUrlInput.value = it },
                    label = { Text("Image URL Link") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { viewModel.addImageUrl(viewModel.currentUrlInput.value) },
                    enabled = viewModel.currentUrlInput.value.isNotBlank()
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Image")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.imageUrls.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp), // Height for the image display area
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.imageUrls) { imageUrl ->
                        Box(
                            modifier = Modifier
                                .size(180.dp, 200.dp) // Individual image size
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = imageUrl),
                                contentDescription = "Travel Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { viewModel.removeImageUrl(imageUrl) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Remove Image",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No images added yet. Add a URL above to see your photos!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Post Details
            Text(
                text = "Post Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.title.value,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.location.value,
                onValueChange = { viewModel.updateLocation(it) },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Review with Stars
            Text(
                text = "Review",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                (1..5).forEach { starIndex ->
                    Icon(
                        imageVector = if (starIndex <= viewModel.reviewRating.value) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Star $starIndex",
                        tint = if (starIndex <= viewModel.reviewRating.value) Color(0xFFFFC107) else Color.Gray, // Gold for filled stars
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { viewModel.updateReviewRating(starIndex) }
                    )
                    if (starIndex < 5) Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.description.value,
                onValueChange = { viewModel.updateDescription(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                singleLine = false,
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.taggedPersons.value,
                onValueChange = { viewModel.updateTaggedPersons(it) },
                label = { Text("Tag People (e.g., @john, @jane)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.createPost() },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Create Post", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreatePostScreen() {
    MaterialTheme {
        CreatePostScreen()
    }
}
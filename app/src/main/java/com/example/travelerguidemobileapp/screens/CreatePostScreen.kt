package com.example.travelerguidemobileapp.screens

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.Close

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.travelerguidemobileapp.viewmodel.CreatePostViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    vm: CreatePostViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        Text(
            text = "Photos",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (vm.imageUrls.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Image,
                    contentDescription = "placeholder",
                    modifier = Modifier.size(64.dp),
                )
            }
        } else {
            val mainUrl = vm.imageUrls.firstOrNull()
            if (!mainUrl.isNullOrBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(mainUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "main photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(vm.imageUrls.size) { idx ->
                    val url = vm.imageUrls[idx]
                    AsyncImage(
                        model = url,
                        contentDescription = "preview $idx",
                        modifier = Modifier
                            .size(88.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic URL inputs
        Text("Image URLs", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(8.dp))

        vm.urlInputs.forEachIndexed { idx, value ->
            var localText by remember { mutableStateOf(TextFieldValue(value)) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = localText,
                    onValueChange = {
                        localText = it
                        vm.updateUrlInput(idx, it.text)
                    },
                    singleLine = true,
                    placeholder = { Text("https://example.com/image.jpg") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {
                    focusManager.clearFocus()
                    vm.commitUrl(idx)
                    vm.addUrlInput()
                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add image")
                }

                if (vm.urlInputs.size > 1) {
                    IconButton(onClick = { vm.removeUrlInput(idx) }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Remove")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vm.title,
            onValueChange = { vm.setTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = vm.location,
            onValueChange = { vm.setLocation(it) },
            label = { Text("Location") },
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Review", fontWeight = FontWeight.Medium, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            repeat(5) { i ->
                val selected = i < vm.rating
                Icon(
                    imageVector = if (selected) Icons.Filled.Star else Icons.Outlined.StarBorder,
                    contentDescription = "star ${i + 1}",
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { vm.setRating(i + 1) }
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = vm.description,
            onValueChange = { vm.setDescription(it) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            maxLines = 8
        )

        Spacer(modifier = Modifier.height(12.dp))

        var tagText by remember { mutableStateOf("") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = tagText,
                onValueChange = { tagText = it },
                label = { Text("Tag person (username)") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (tagText.isNotBlank()) {
                    vm.addTag(tagText)
                    tagText = ""
                }
            }) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            vm.tags.forEach { t ->
                AssistChip(
                    onClick = { },
                    label = { Text(t) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "remove",
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { vm.removeTag(t) }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { vm.submitPost() },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text("Publish")
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

package com.example.travelerguidemobileapp.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.lifecycle.viewmodel.compose.viewModel // To instantiate ViewModel
import com.example.travelerguidemobileapp.viewmodel.NewDestinationViewModel
import com.example.travelerguidemobileapp.viewmodel.CreationState // For checking success/error
// Import real data model classes
import com.example.travelerguidemobileapp.data.model.Category
import com.example.travelerguidemobileapp.data.model.Subcategory
import com.example.travelerguidemobileapp.data.util.DummySubcategory // For SubcategoryDropdown (temporarily reusing dummy)
import com.example.travelerguidemobileapp.data.util.DummyCategory // For CategorySelection (temporarily reusing dummy)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDestinationScreen(
    navController: NavController,
    // Inject the ViewModel here (will require a factory when instantiated in NavHost)
    viewModel: NewDestinationViewModel = viewModel()
) {
    // Collect state flows from ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val creationState by viewModel.creationState.collectAsState()

    // Collect user input flows
    val destinationName by viewModel.destinationNameInput.collectAsState()
    val description by viewModel.descriptionInput.collectAsState()
    val googleMapsUrl by viewModel.googleMapsUrlInput.collectAsState()
    val initialRule by viewModel.initialRuleInput.collectAsState()
    val imageUri by viewModel.imageUriInput.collectAsState()
    val selectedCategoryId by viewModel.selectedCategoryId.collectAsState()
    val selectedSubcategoryId by viewModel.selectedSubcategoryId.collectAsState()
    val newSubcategoryName by viewModel.newSubcategoryNameInput.collectAsState()


    // Handle side effects (Success/Error)
    LaunchedEffect(creationState) {
        when (creationState) {
            is CreationState.Success -> {
                navController.popBackStack()
                // Optionally show a toast: Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
            }
            is CreationState.Error -> {
                // Show Error Message/Snackbar
                println("Creation Error: ${(creationState as CreationState.Error).message}")
            }
            else -> {}
        }
    }


    // State for image selection
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.imageUriInput.value = uri // Update ViewModel input
    }

    // Filter subcategories (this logic should ideally move to ViewModel, but we filter the UI state here)
    val filteredSubcategories = remember(selectedCategoryId, uiState.subcategories) {
        // Find subcategories matching the selected category ID
        uiState.subcategories.filter { it.parentCategoryId == selectedCategoryId }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Destination") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // --- 1. CATEGORY SELECTION ---
            item {
                Spacer(Modifier.height(16.dp))
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("Step 1: Choose Main Category", style = MaterialTheme.typography.titleMedium)
                    CategorySelection(
                        categories = uiState.categories, // Use real data from ViewModel
                        selectedId = selectedCategoryId,
                        onSelect = { id ->
                            viewModel.selectedCategoryId.value = id
                            viewModel.selectedSubcategoryId.value = null
                            viewModel.newSubcategoryNameInput.value = ""
                            viewModel.loadSubcategories(id) // Load subcategories when category changes
                        }
                    )
                }
                Spacer(Modifier.height(24.dp))
            }

            // --- 2. SUBCATEGORY SELECTION/CREATION ---
            if (selectedCategoryId != null) {
                item {
                    Text("Step 2: Choose Subcategory or Create New", style = MaterialTheme.typography.titleMedium)
                    Divider(Modifier.padding(vertical = 8.dp))

                    // Dropdown for EXISTING Subcategories
                    SubcategoryDropdown(
                        subcategories = filteredSubcategories, // Use filtered data
                        selectedId = selectedSubcategoryId,
                        onSelect = { id ->
                            viewModel.selectedSubcategoryId.value = id
                            viewModel.newSubcategoryNameInput.value = ""
                        }
                    )

                    Text("OR", Modifier.padding(vertical = 8.dp), style = MaterialTheme.typography.bodySmall)

                    // Input for NEW Subcategory
                    OutlinedTextField(
                        value = newSubcategoryName,
                        onValueChange = {
                            viewModel.newSubcategoryNameInput.value = it
                            if (it.isNotBlank()) viewModel.selectedSubcategoryId.value = null
                        },
                        label = { Text("Create New Subcategory") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }

            // --- 3. DESTINATION DETAILS & GUIDE PREVIEW ---
            item {
                Text("Step 3: Define the Destination Details", style = MaterialTheme.typography.titleMedium)
                Divider(Modifier.padding(vertical = 8.dp))

                // A. IMAGE SELECTION
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Destination Cover Image")
                }

                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Selected Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 8.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("No Image Selected (Required)", style = MaterialTheme.typography.bodySmall, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                }
                Spacer(Modifier.height(8.dp))

                // B. DESTINATION NAME
                OutlinedTextField(
                    value = destinationName,
                    onValueChange = { viewModel.destinationNameInput.value = it },
                    label = { Text("Specific Destination Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                // C. DESCRIPTION
                OutlinedTextField(
                    value = description,
                    onValueChange = { viewModel.descriptionInput.value = it },
                    label = { Text("Short Destination Description") },
                    singleLine = false,
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
                )
                Spacer(Modifier.height(16.dp))

                // D. LOCATION (Google Maps URL)
                OutlinedTextField(
                    value = googleMapsUrl,
                    onValueChange = { viewModel.googleMapsUrlInput.value = it },
                    label = { Text("Google Maps URL (e.g., Share Link)") },
                    placeholder = { Text("https://maps.app.goo.gl/...") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))

                // E. INITIAL GUIDE RULE
                OutlinedTextField(
                    value = initialRule,
                    onValueChange = { viewModel.initialRuleInput.value = it },
                    label = { Text("Initial Mandatory Rule/Description") },
                    placeholder = { Text("Ex: Must carry a valid entry pass.") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp)
                )
                Spacer(Modifier.height(32.dp))

                // --- SUBMIT BUTTON ---
                val isSubcategoryValid = selectedSubcategoryId != null || (newSubcategoryName.isNotBlank() && selectedCategoryId != null)
                val isFormValid = destinationName.isNotBlank() && selectedCategoryId != null && isSubcategoryValid && imageUri != null && description.isNotBlank() && googleMapsUrl.isNotBlank()

                Button(
                    onClick = {
                        // Call the ViewModel's submission logic
                        // NOTE: You must replace "user_id_placeholder" with the actual current user ID later.
                        viewModel.submitDestinationAndGuide("user_id_placeholder")
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    enabled = isFormValid && creationState != CreationState.Loading
                ) {
                    if (creationState == CreationState.Loading) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Create Guide & Continue")
                    }
                }
            }
        }
    }
}

// --- Reusable Dropdown Components (Updated to use real data models) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelection(categories: List<Category>, selectedId: String?, onSelect: (String) -> Unit) {
    // Logic remains the same, but uses the real Category model
    var expanded by remember { mutableStateOf(false) }
    val currentName = categories.find { it.id == selectedId }?.name ?: "Select Main Category"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        OutlinedTextField(
            value = currentName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Main Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = { onSelect(category.id); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubcategoryDropdown(subcategories: List<Subcategory>, selectedId: String?, onSelect: (String) -> Unit) {
    // Logic remains the same, but uses the real Subcategory model
    var expanded by remember { mutableStateOf(false) }
    val currentName = subcategories.find { it.id == selectedId }?.name ?: "Select Existing Subcategory"

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        OutlinedTextField(
            value = currentName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Existing Subcategory") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            subcategories.forEach { sub ->
                DropdownMenuItem(
                    text = { Text(sub.name) },
                    onClick = { onSelect(sub.id); expanded = false }
                )
            }
        }
    }
}
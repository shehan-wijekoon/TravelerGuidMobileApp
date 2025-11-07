package com.example.travelerguidemobileapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travelerguidemobileapp.data.model.*
import com.example.travelerguidemobileapp.data.repository.DestinationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.net.Uri // Required for image URI

// --- State Definitions (Unchanged) ---

sealed class CreationState {
    object Idle : CreationState()
    object Loading : CreationState()
    data class Success(val newDestinationId: String) : CreationState()
    data class Error(val message: String) : CreationState()
}

data class CreationUiState(
    val categories: List<Category> = emptyList(),
    val subcategories: List<Subcategory> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// --- ViewModel Implementation ---

class NewDestinationViewModel(
    // Note: This dependency must be provided via a ViewModelFactory or Hilt later.
    private val repository: DestinationsRepository
) : ViewModel() {

    // --- Core Creation State (Unchanged) ---
    private val _creationState = MutableStateFlow<CreationState>(CreationState.Idle)
    val creationState: StateFlow<CreationState> = _creationState

    // --- UI Data State (Categories/Subcategories - Unchanged) ---
    private val _uiState = MutableStateFlow(CreationUiState())
    val uiState: StateFlow<CreationUiState> = _uiState

    // --- User Input State (Connected to UI TextFields) ---
    val destinationNameInput = MutableStateFlow("")
    val descriptionInput = MutableStateFlow("")

    // ❌ REMOVED: val latitudeInput = MutableStateFlow(0.0)
    // ❌ REMOVED: val longitudeInput = MutableStateFlow(0.0)

    // ✅ ADDED: Google Maps URL Input
    val googleMapsUrlInput = MutableStateFlow("")

    val imageUriInput = MutableStateFlow<Uri?>(null) // URI for the selected cover image

    val selectedCategoryId = MutableStateFlow<String?>(null)
    val selectedSubcategoryId = MutableStateFlow<String?>(null)
    val newSubcategoryNameInput = MutableStateFlow("")
    val initialRuleInput = MutableStateFlow("") // For the basic UserGuide

    init {
        // Start loading top categories immediately when the screen opens
        loadCategories()
    }

    // 1. Load Main Categories (Jungle, Beach - Unchanged)
    private fun loadCategories() = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true)
        try {
            repository.getCategories().collect { categories ->
                _uiState.value = _uiState.value.copy(categories = categories, isLoading = false)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = "Failed to load categories.", isLoading = false)
        }
    }

    // 2. Load Subcategories based on selected Main Category (Unchanged)
    fun loadSubcategories(categoryId: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(isLoading = true, subcategories = emptyList())
        try {
            repository.getSubcategoriesByParent(categoryId).collect { subs ->
                _uiState.value = _uiState.value.copy(subcategories = subs, isLoading = false)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = "Failed to load subcategories.", isLoading = false)
        }
    }

    /**
     * Main Submission Logic: Creates Subcategory (if new), Destination, and UserGuide.
     */
    fun submitDestinationAndGuide(userId: String) {
        // BASIC VALIDATION: Updated to check googleMapsUrlInput
        if (destinationNameInput.value.isBlank() || selectedCategoryId.value == null || descriptionInput.value.isBlank() || imageUriInput.value == null || googleMapsUrlInput.value.isBlank()) {
            _creationState.value = CreationState.Error("Please fill all required fields including image, category, and location URL.")
            return
        }

        _creationState.value = CreationState.Loading
        viewModelScope.launch {
            try {
                // A. Determine final Subcategory ID (creating if necessary - Unchanged)
                var finalSubId = selectedSubcategoryId.value

                if (newSubcategoryNameInput.value.isNotBlank() && finalSubId == null) {
                    val newSubcategory = Subcategory(
                        parentCategoryId = selectedCategoryId.value!!,
                        name = newSubcategoryNameInput.value.trim()
                    )
                    finalSubId = repository.createSubcategory(newSubcategory)
                }

                if (finalSubId == null) {
                    _creationState.value = CreationState.Error("A subcategory must be selected or created.")
                    return@launch
                }

                // B. Upload image and create the new Destination
                val coverImageUrl = repository.uploadDestinationCoverImage(imageUriInput.value!!)

                // NOTE: Since the Destination model likely still requires latitude/longitude,
                // we will use placeholder values (0.0) and include the URL in the description
                // or a dedicated field if the model supported it.
                // For a proper solution, you should update the Destination data class
                // to include 'googleMapsUrl: String'.

                val newDestination = Destination(
                    name = destinationNameInput.value.trim(),
                    subcategoryId = finalSubId,
                    // Keeping these for compilation, assuming 0.0 is acceptable placeholder for now
                    latitude = 0.0,
                    longitude = 0.0,
                    // Temporarily append the URL to the description for transmission
                    description = "${descriptionInput.value.trim()}\n\n[MAP URL]: ${googleMapsUrlInput.value.trim()}",
                    coverImageUrl = coverImageUrl,
                    creatorId = userId
                )
                val newDestId = repository.createDestination(newDestination)

                // C. Create the initial User Guide (Unchanged)
                val initialGuide = UserGuide(
                    destinationId = newDestId,
                    rulesAndRegulations = initialRuleInput.value
                )
                repository.createUserGuide(initialGuide)

                _creationState.value = CreationState.Success(newDestId)

            } catch (e: Exception) {
                _creationState.value = CreationState.Error("Submission failed: ${e.localizedMessage}")
            }
        }
    }
}
package com.example.travelguidemobileapp.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreatePostViewModel : ViewModel() {

    // State for image URLs
    private val _imageUrls = mutableStateListOf<String>()
    val imageUrls: List<String> get() = _imageUrls

    // State for the current URL input field
    val currentUrlInput = mutableStateOf("")

    // State for post details
    val title = mutableStateOf("")
    val location = mutableStateOf("")
    val reviewRating = mutableStateOf(0) // 0 to 5 stars
    val description = mutableStateOf("")
    val taggedPersons = mutableStateOf("") // Comma-separated or similar

    fun addImageUrl(url: String) {
        if (url.isNotBlank() && !_imageUrls.contains(url)) {
            _imageUrls.add(url)
            currentUrlInput.value = "" // Clear input after adding
        }
    }

    fun removeImageUrl(url: String) {
        _imageUrls.remove(url)
    }

    fun updateTitle(newTitle: String) {
        title.value = newTitle
    }

    fun updateLocation(newLocation: String) {
        location.value = newLocation
    }

    fun updateReviewRating(rating: Int) {
        reviewRating.value = rating.coerceIn(0, 5) // Ensure rating is between 0 and 5
    }

    fun updateDescription(newDescription: String) {
        description.value = newDescription
    }

    fun updateTaggedPersons(newTaggedPersons: String) {
        taggedPersons.value = newTaggedPersons
    }

    fun createPost() {
        // Here you would typically send this data to a repository or API
        // For now, let's just print it to the console
        println("Creating Post:")
        println("Image URLs: $imageUrls")
        println("Title: ${title.value}")
        println("Location: ${location.value}")
        println("Review Rating: ${reviewRating.value} stars")
        println("Description: ${description.value}")
        println("Tagged Persons: ${taggedPersons.value}")

        // You would likely navigate away or show a success message here
    }
}
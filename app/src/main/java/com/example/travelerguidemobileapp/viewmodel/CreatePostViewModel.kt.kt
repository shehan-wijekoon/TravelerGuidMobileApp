package com.example.travelerguidemobileapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {

    //a dynamic list
    private val _imageUrls = mutableStateListOf<String>()
    val imageUrls: List<String> get() = _imageUrls

    //tem text for the currently foused Url input field

    private val _urlInputs = mutableStateListOf<String>()
    val urlInputs: List<String> get() = _urlInputs

    // form fields

    var title by mutableStateOf("")
        private set

    var location by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var rating by mutableStateOf(0)
        private set

    //tags
    private val _tags = mutableStateListOf<String>()
    val tags: List<String> get() = _tags

    init {
        // Start with one URL input visiable
        _urlInputs.add("")
    }

    fun updateUrInputs(index: Int, value: String) {
        if (index in _urlInputs.indices) _urlInputs[index] = value
    }

    fun addUrlInput() {
        _urlInputs.add("") // new empty textbox
    }

    fun removeUrlInput(index: Int) {
        if (index in _urlInputs.indices) {
            _urlInputs.removeAt(index)

            // also remove associated image (if already added)
            if (index < _imageUrls.size) _imageUrls.removeAt(index)
        }
    }

    // user clicks + near to get a input field
    fun commitUrl(index: Int) {
        if (index in _urlInputs.indices) {
            val url = _urlInputs[index].trim()
            if (url.isNotEmpty()) {
                // if there is already an image at this index, replace; otherwise append
                if (index < _imageUrls.size) {
                    _imageUrls[index] = url
                } else {
                    // ensure list keeps same index mapping
                    while (_imageUrls.size < index) _imageUrls.add("")
                    _imageUrls.add(url)
                }
            }
        }
    }
    fun setTitle(value: String) { title = value }
    fun setLocation(value: String) { location = value }
    fun setDescription(value: String) { description = value }

    fun setRating(value: Int) { rating = value.coerceIn(0, 5) }

    fun addTag(tag: String) {
        val t = tag.trim()
        if (t.isNotEmpty() && !_tags.contains(t)) _tags.add(t)
    }

    fun removeTag(tag: String) { _tags.remove(tag) }

    // optional: submit post logic (validate & send to backend)
    fun submitPost() : Boolean {
        // Add validation as needed. Here just check basic non-empty title and at least one image.
        if (title.isBlank()) return false
        if (_imageUrls.isEmpty()) return false

        // Here, you would prepare post payload and call repository / network layer.
        return true
    }
}

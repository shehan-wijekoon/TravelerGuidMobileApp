package com.example.travelerguidemobileapp.data.model

import com.google.firebase.firestore.DocumentId

data class Subcategory(
    @DocumentId
    val id: String = "",
    val parentCategoryId: String = "",
    val name: String = "",
    val imageUrl: String = ""
)
package com.example.travelerguidemobileapp.data.model

import com.google.firebase.firestore.DocumentId

data class Category(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val imageUrl: String = ""
)
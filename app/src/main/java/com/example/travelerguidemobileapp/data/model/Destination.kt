package com.example.travelerguidemobileapp.data.model

import com.google.firebase.firestore.DocumentId

data class Destination(
    @DocumentId
    val id: String = "",
    val subcategoryId: String = "",
    val name: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val description: String = "",
    val googleMapsUrl: String = "",
    val coverImageUrl: String = "",
    val creatorId: String = ""
)
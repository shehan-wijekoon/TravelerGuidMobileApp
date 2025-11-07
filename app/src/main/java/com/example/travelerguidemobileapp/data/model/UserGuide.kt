package com.example.travelerguidemobileapp.data.model

import com.google.firebase.firestore.DocumentId

data class UserGuide(
    // We use the Destination ID as the document ID for a 1:1 relationship
    @DocumentId
    val destinationId: String = "",
    val howToTravel: String = "", // Detailed transport info
    val placesToSee: List<String> = emptyList(), // List of attractions
    val rulesAndRegulations: String = "", // The initial rule input from the UI
    val bestTimeToVisit: String = ""
)
package com.example.travelerguidemobileapp.data.util

// --- Data Classes for Pure UI Mode ---

data class DummyCategory(val id: String, val name: String)
data class DummySubcategory(val id: String, val name: String, val parentId: String)
data class DummyDestination(
    val id: String,
    val name: String,
    val subcategoryId: String,
    val description: String,
    val googleMapsUrl: String,
    val coverImageUrl: String
)


// --- Dummy Data Lists (Consolidated) ---

val dummyCategories = listOf(
    DummyCategory("C1", "Jungle"),
    DummyCategory("C2", "Beach"),
    DummyCategory("C3", "Mountain"),
    DummyCategory("C4", "City Break"),
    DummyCategory("C5", "Desert")
)

val dummySubcategoriesList = listOf(
    DummySubcategory("S1", "Amazon Rainforest", "C1"), // Jungle
    DummySubcategory("S2", "Bali Coasts", "C2"),      // Beach
    DummySubcategory("S4", "Gili Islands", "C2"),     // Beach
    DummySubcategory("S3", "Himalayan Foothills", "C3")// Mountain
)

val dummyDestinations = listOf(
    DummyDestination(
        id = "d1",
        name = "Hidden Bali Waterfall",
        subcategoryId = "S2",
        description = "A remote, beautiful waterfall popular with guides.",
        googleMapsUrl = "https://maps.app.goo.gl/W5NqX92d6z8gQ83X7",
        coverImageUrl = "https://picsum.photos/seed/bali/400/300"
    ),
    DummyDestination(
        id = "d2",
        name = "Gili Island Sunset Spot",
        subcategoryId = "S4",
        description = "Best spot to watch the sun go down.",
        googleMapsUrl = "https://maps.app.goo.gl/G8pZ7h7kX4jA7P349",
        coverImageUrl = "https://picsum.photos/seed/gili/400/300"
    ),
    DummyDestination(
        id = "d3",
        name = "Amazon River Cruise Port",
        subcategoryId = "S1",
        description = "The main port for jungle excursions.",
        googleMapsUrl = "https://maps.app.goo.gl/A4dZ9h2kX4jA7P341",
        coverImageUrl = "https://picsum.photos/seed/amazon/400/300"
    )
)
package com.example.travelerguidemobileapp.controllers

sealed class Screen(val route: String) {

    object MainCategoryDisplay : Screen("main_category_display")

    object SubcategoryList : Screen("subcategory_list/{catId}/{catName}")

    object NewDestination : Screen("new_destination")

    object SubcategoryDisplay : Screen("subcategory_display/{subId}/{subName}")

    // Helper functions to create the actual path for navigation
    fun createSubcategoryListRoute(catId: String, catName: String): String {
        return "subcategory_list/$catId/${java.net.URLEncoder.encode(catName, "UTF-8")}"
    }

    fun createSubcategoryDisplayRoute(subId: String, subName: String): String {
        return "subcategory_display/$subId/${java.net.URLEncoder.encode(subName, "UTF-8")}"
    }
}
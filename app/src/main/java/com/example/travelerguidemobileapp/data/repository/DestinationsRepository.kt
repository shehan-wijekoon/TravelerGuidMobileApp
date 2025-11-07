package com.example.travelerguidemobileapp.data.repository

import android.net.Uri
import com.example.travelerguidemobileapp.data.model.*
import kotlinx.coroutines.flow.Flow

// Changed from 'class' to 'interface'
interface DestinationsRepository {
    // ... existing read/write methods

    // 🎯 ADD THIS NEW METHOD
    suspend fun getDestinationsBySubcategory(subcategoryId: String): Flow<List<Destination>>
}
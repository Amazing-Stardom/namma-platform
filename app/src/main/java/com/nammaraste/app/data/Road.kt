package com.nammaraste.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roads")
data class Road(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val roadName: String,
    val location: String,
    val contractorName: String,
    val contactNumber: String,
    val warrantyPeriod: String, // E.g., "5 years"
    val healthScore: Int
)

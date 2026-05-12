package com.nammaraste.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roads")
data class RoadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roadName: String,           // e.g. "Hosur-Bagalur Main Road"
    val location: String,           // e.g. "Hosur Taluka, Krishnagiri"
    val totalLengthKm: Float,       // e.g. 12.5
    val constructedYear: Int,       // e.g. 2019
    val healthScore: Int,           // 0–100 (starts at 100, decreases per report)
    val latitude: Double,           // road start point latitude
    val longitude: Double,          // road start point longitude
    val endLatitude: Double,        // road end point latitude
    val endLongitude: Double        // road end point longitude
)

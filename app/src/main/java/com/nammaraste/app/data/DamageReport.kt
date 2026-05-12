package com.nammaraste.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "damage_reports")
data class DamageReport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val roadId: Int,
    val photoUri: String?, // Representing the photo
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val timestamp: Long
)

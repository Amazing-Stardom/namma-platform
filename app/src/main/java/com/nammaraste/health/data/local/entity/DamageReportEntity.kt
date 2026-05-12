package com.nammaraste.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "damage_reports")
data class DamageReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roadId: Int,                // Foreign key → RoadEntity.id
    val description: String,        // Citizen's description of damage
    val photoUri: String,           // URI path of captured photo
    val latitude: Double,           // GPS lat at time of report
    val longitude: Double,          // GPS lng at time of report
    val timestamp: Long,            // System.currentTimeMillis()
    val reporterName: String        // Optional reporter name
)

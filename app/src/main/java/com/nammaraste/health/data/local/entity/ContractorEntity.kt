package com.nammaraste.health.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contractors")
data class ContractorEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val roadId: Int,                // Foreign key → RoadEntity.id
    val contractorName: String,     // e.g. "Sri Balaji Constructions"
    val contractorPhone: String,    // e.g. "+91 98765 43210"
    val contractorEmail: String,    // e.g. "balaji@constructions.com"
    val contractValue: String,      // e.g. "₹45,00,000"
    val startDate: String,          // e.g. "15 March 2019"
    val completionDate: String,     // e.g. "20 August 2019"
    val warrantyPeriod: String,     // e.g. "5 Years (until 2024)"
    val warrantyExpired: Boolean    // true if warranty has expired
)

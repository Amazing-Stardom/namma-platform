package com.nammaraste.health.data.local.dao

import androidx.room.*
import com.nammaraste.health.data.local.entity.DamageReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DamageReportDao {
    @Query("SELECT * FROM damage_reports WHERE roadId = :roadId ORDER BY timestamp DESC")
    fun getReportsForRoad(roadId: Int): Flow<List<DamageReportEntity>>

    @Query("SELECT * FROM damage_reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<DamageReportEntity>>

    @Query("SELECT COUNT(*) FROM damage_reports WHERE roadId = :roadId")
    fun getReportCountForRoad(roadId: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: DamageReportEntity)
}

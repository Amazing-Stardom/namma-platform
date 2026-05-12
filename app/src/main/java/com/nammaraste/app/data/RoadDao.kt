package com.nammaraste.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadDao {
    @Query("SELECT * FROM roads")
    fun getAllRoads(): Flow<List<Road>>

    @Query("SELECT * FROM roads WHERE roadName LIKE '%' || :searchQuery || '%'")
    fun searchRoads(searchQuery: String): Flow<List<Road>>

    @Query("SELECT * FROM roads WHERE id = :roadId")
    suspend fun getRoadById(roadId: Int): Road?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoads(roads: List<Road>)

    @Update
    suspend fun updateRoad(road: Road)

    @Insert
    suspend fun insertDamageReport(report: DamageReport)
}

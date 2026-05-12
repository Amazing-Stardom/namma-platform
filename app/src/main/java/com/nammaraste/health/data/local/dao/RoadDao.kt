package com.nammaraste.health.data.local.dao

import androidx.room.*
import com.nammaraste.health.data.local.entity.RoadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoadDao {
    @Query("SELECT * FROM roads ORDER BY healthScore ASC")
    fun getAllRoads(): Flow<List<RoadEntity>>

    @Query("SELECT * FROM roads WHERE roadName LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchRoads(query: String): Flow<List<RoadEntity>>

    @Query("SELECT * FROM roads WHERE id = :id")
    fun getRoadById(id: Int): Flow<RoadEntity?>

    @Query("SELECT * FROM roads WHERE healthScore > 50 ORDER BY healthScore DESC")
    fun getHealthyRoads(): Flow<List<RoadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoad(road: RoadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRoads(roads: List<RoadEntity>)

    @Update
    suspend fun updateRoad(road: RoadEntity)

    @Query("UPDATE roads SET healthScore = healthScore - 10 WHERE id = :roadId AND healthScore > 0")
    suspend fun decreaseHealthScore(roadId: Int)

    @Query("SELECT COUNT(*) FROM roads")
    suspend fun getRoadCount(): Int
}

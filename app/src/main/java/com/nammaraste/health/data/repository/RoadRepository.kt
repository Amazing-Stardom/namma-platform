package com.nammaraste.health.data.repository

import com.nammaraste.health.data.local.dao.RoadDao
import com.nammaraste.health.data.local.entity.RoadEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoadRepository @Inject constructor(private val roadDao: RoadDao) {
    fun getAllRoads(): Flow<List<RoadEntity>> = roadDao.getAllRoads()
    fun searchRoads(query: String): Flow<List<RoadEntity>> = roadDao.searchRoads(query)
    fun getRoadById(id: Int): Flow<RoadEntity?> = roadDao.getRoadById(id)
    fun getHealthyRoads(): Flow<List<RoadEntity>> = roadDao.getHealthyRoads()
    suspend fun insertRoad(road: RoadEntity) = roadDao.insertRoad(road)
    suspend fun decreaseHealthScore(roadId: Int) = roadDao.decreaseHealthScore(roadId)
    suspend fun getRoadCount(): Int = roadDao.getRoadCount()
}

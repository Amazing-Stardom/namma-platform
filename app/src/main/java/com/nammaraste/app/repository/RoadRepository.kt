package com.nammaraste.app.repository

import com.nammaraste.app.data.DamageReport
import com.nammaraste.app.data.Road
import com.nammaraste.app.data.RoadDao
import kotlinx.coroutines.flow.Flow

class RoadRepository(private val roadDao: RoadDao) {

    val allRoads: Flow<List<Road>> = roadDao.getAllRoads()

    fun searchRoads(query: String): Flow<List<Road>> {
        return roadDao.searchRoads(query)
    }

    suspend fun getRoadById(roadId: Int): Road? {
        return roadDao.getRoadById(roadId)
    }

    suspend fun addDamageReport(report: DamageReport) {
        roadDao.insertDamageReport(report)
        val road = roadDao.getRoadById(report.roadId)
        if (road != null) {
            val updatedScore = (road.healthScore - 10).coerceAtLeast(0)
            roadDao.updateRoad(road.copy(healthScore = updatedScore))
        }
    }
}

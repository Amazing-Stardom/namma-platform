package com.nammaraste.health.data.repository

import com.nammaraste.health.data.local.dao.DamageReportDao
import com.nammaraste.health.data.local.entity.DamageReportEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DamageReportRepository @Inject constructor(private val dao: DamageReportDao) {
    fun getReportsForRoad(roadId: Int): Flow<List<DamageReportEntity>> = dao.getReportsForRoad(roadId)
    fun getAllReports(): Flow<List<DamageReportEntity>> = dao.getAllReports()
    fun getReportCount(roadId: Int): Flow<Int> = dao.getReportCountForRoad(roadId)
    suspend fun insertReport(report: DamageReportEntity) = dao.insertReport(report)
}

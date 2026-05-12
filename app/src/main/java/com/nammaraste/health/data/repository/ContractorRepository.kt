package com.nammaraste.health.data.repository

import com.nammaraste.health.data.local.dao.ContractorDao
import com.nammaraste.health.data.local.entity.ContractorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContractorRepository @Inject constructor(private val dao: ContractorDao) {
    fun getContractorForRoad(roadId: Int): Flow<ContractorEntity?> = dao.getContractorForRoad(roadId)
    suspend fun insertContractor(c: ContractorEntity) = dao.insertContractor(c)
}

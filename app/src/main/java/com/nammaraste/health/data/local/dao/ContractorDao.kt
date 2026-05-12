package com.nammaraste.health.data.local.dao

import androidx.room.*
import com.nammaraste.health.data.local.entity.ContractorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractorDao {
    @Query("SELECT * FROM contractors WHERE roadId = :roadId")
    fun getContractorForRoad(roadId: Int): Flow<ContractorEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContractor(contractor: ContractorEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllContractors(contractors: List<ContractorEntity>)

    @Query("SELECT COUNT(*) FROM contractors")
    suspend fun getContractorCount(): Int
}

package com.nammaraste.health.ui.screen.contractorinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammaraste.health.data.local.entity.ContractorEntity
import com.nammaraste.health.data.local.entity.RoadEntity
import com.nammaraste.health.data.repository.ContractorRepository
import com.nammaraste.health.data.repository.DamageReportRepository
import com.nammaraste.health.data.repository.RoadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractorInfoViewModel @Inject constructor(
    private val contractorRepository: ContractorRepository,
    private val roadRepository: RoadRepository,
    private val damageReportRepository: DamageReportRepository
) : ViewModel() {

    data class ContractorInfoUiState(
        val road: RoadEntity? = null,
        val contractor: ContractorEntity? = null,
        val reportCount: Int = 0,
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(ContractorInfoUiState())
    val uiState: StateFlow<ContractorInfoUiState> = _uiState

    fun loadData(roadId: Int) {
        viewModelScope.launch {
            combine(
                roadRepository.getRoadById(roadId),
                contractorRepository.getContractorForRoad(roadId),
                damageReportRepository.getReportCount(roadId)
            ) { road, contractor, count ->
                ContractorInfoUiState(road, contractor, count, false)
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
}

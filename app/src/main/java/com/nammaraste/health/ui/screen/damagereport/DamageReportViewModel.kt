package com.nammaraste.health.ui.screen.damagereport

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammaraste.health.data.local.entity.DamageReportEntity
import com.nammaraste.health.data.local.entity.RoadEntity
import com.nammaraste.health.data.repository.DamageReportRepository
import com.nammaraste.health.data.repository.RoadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DamageReportViewModel @Inject constructor(
    private val damageReportRepository: DamageReportRepository,
    private val roadRepository: RoadRepository
) : ViewModel() {

    data class DamageReportUiState(
        val road: RoadEntity? = null,
        val description: String = "",
        val photoUri: Uri? = null,
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val reporterName: String = "",
        val isSubmitting: Boolean = false,
        val isSubmitted: Boolean = false,
        val errorMessage: String? = null,
        val existingReports: List<DamageReportEntity> = emptyList()
    )

    private val _uiState = MutableStateFlow(DamageReportUiState())
    val uiState: StateFlow<DamageReportUiState> = _uiState

    fun loadRoad(roadId: Int) {
        viewModelScope.launch {
            roadRepository.getRoadById(roadId).collect { road ->
                _uiState.update { it.copy(road = road) }
            }
        }
        viewModelScope.launch {
            damageReportRepository.getReportsForRoad(roadId).collect { reports ->
                _uiState.update { it.copy(existingReports = reports) }
            }
        }
    }

    fun onDescriptionChange(desc: String) {
        _uiState.update { it.copy(description = desc) }
    }

    fun onReporterNameChange(name: String) {
        _uiState.update { it.copy(reporterName = name) }
    }

    fun onPhotoTaken(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    fun onLocationCaptured(lat: Double, lng: Double) {
        _uiState.update { it.copy(latitude = lat, longitude = lng) }
    }

    fun submitReport() {
        val state = _uiState.value
        if (state.road == null || state.description.isEmpty() || state.photoUri == null || state.latitude == 0.0) {
            _uiState.update { it.copy(errorMessage = "Please fill all required fields and capture location/photo") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            val report = DamageReportEntity(
                roadId = state.road.id,
                description = state.description,
                photoUri = state.photoUri.toString(),
                latitude = state.latitude,
                longitude = state.longitude,
                timestamp = System.currentTimeMillis(),
                reporterName = state.reporterName
            )
            damageReportRepository.insertReport(report)
            roadRepository.decreaseHealthScore(state.road.id)
            _uiState.update { it.copy(isSubmitting = false, isSubmitted = true) }
        }
    }
}

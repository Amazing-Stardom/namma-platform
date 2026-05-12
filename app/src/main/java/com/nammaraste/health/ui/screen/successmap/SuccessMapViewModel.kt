package com.nammaraste.health.ui.screen.successmap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nammaraste.health.data.local.entity.RoadEntity
import com.nammaraste.health.data.repository.RoadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SuccessMapViewModel @Inject constructor(
    private val roadRepository: RoadRepository
) : ViewModel() {

    val allRoads: StateFlow<List<RoadEntity>> = roadRepository
        .getAllRoads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val healthyRoads: StateFlow<List<RoadEntity>> = roadRepository
        .getHealthyRoads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}

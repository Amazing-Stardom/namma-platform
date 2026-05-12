package com.nammaraste.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nammaraste.app.data.AppDatabase
import com.nammaraste.app.data.DamageReport
import com.nammaraste.app.data.Road
import com.nammaraste.app.repository.RoadRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RoadViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RoadRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val roadDao = AppDatabase.getDatabase(application).roadDao()
        repository = RoadRepository(roadDao)
    }

    val allRoads: StateFlow<List<Road>> = repository.allRoads
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val filteredRoads: StateFlow<List<Road>> = _searchQuery
        .flatMapLatest { query ->
            repository.searchRoads(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun reportDamage(roadId: Int, photoUri: String?, lat: Double, lng: Double, desc: String) {
        viewModelScope.launch {
            val report = DamageReport(
                roadId = roadId,
                photoUri = photoUri,
                latitude = lat,
                longitude = lng,
                description = desc,
                timestamp = System.currentTimeMillis()
            )
            repository.addDamageReport(report)
        }
    }
    
    suspend fun getRoadById(roadId: Int): Road? {
        return repository.getRoadById(roadId)
    }
}

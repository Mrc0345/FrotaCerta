package com.marcus.frotacerta.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import com.marcus.frotacerta.data.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehicleViewModel(
    private val repository: VehicleRepository
) : ViewModel() {

    val vehicles: StateFlow<List<VehicleEntity>> =
        repository.getAllVehicles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    val availableVehicles: StateFlow<List<VehicleEntity>> =
        repository.getAvailableVehicles()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun insertVehicle(vehicle: VehicleEntity) {
        viewModelScope.launch {
            repository.insertVehicle(vehicle)
        }
    }

    fun updateVehicle(vehicle: VehicleEntity) {
        viewModelScope.launch {
            repository.updateVehicle(vehicle)
        }
    }

    fun updateStatus(
        vehicleId: Long,
        status: String
    ) {
        viewModelScope.launch {
            repository.updateVehicleStatus(
                vehicleId = vehicleId,
                status = status
            )
        }
    }
}

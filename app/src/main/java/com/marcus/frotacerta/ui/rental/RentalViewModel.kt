package com.marcus.frotacerta.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import com.marcus.frotacerta.data.repository.RentalRepository
import com.marcus.frotacerta.data.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RentalViewModel(
    private val rentalRepository: RentalRepository,
    vehicleRepository: VehicleRepository
) : ViewModel() {

    val availableVehicles:
        StateFlow<List<VehicleEntity>> =
        vehicleRepository
            .getAvailableVehicles()
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun createRental(
        contactId: Long,
        clientName: String,
        clientPhone: String,
        vehicleId: Long,
        startDate: Long,
        expectedReturnDate: Long,
        dailyRate: Double,
        estimatedTotal: Double,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                rentalRepository.createRental(
                    contactId = contactId,
                    clientName = clientName,
                    clientPhone = clientPhone,
                    vehicleId = vehicleId,
                    startDate = startDate,
                    expectedReturnDate =
                        expectedReturnDate,
                    dailyRate = dailyRate,
                    estimatedTotal = estimatedTotal
                )

                onSuccess()

            } catch (exception: Exception) {

                onError(
                    exception.message
                        ?: "Não foi possível salvar a locação."
                )
            }
        }
    }
}

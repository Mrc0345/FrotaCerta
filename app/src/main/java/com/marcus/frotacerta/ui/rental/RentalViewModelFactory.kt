package com.marcus.frotacerta.ui.rental

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcus.frotacerta.data.repository.RentalRepository
import com.marcus.frotacerta.data.repository.VehicleRepository

class RentalViewModelFactory(
    private val rentalRepository:
        RentalRepository,
    private val vehicleRepository:
        VehicleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                RentalViewModel::class.java
            )
        ) {

            return RentalViewModel(
                rentalRepository =
                    rentalRepository,
                vehicleRepository =
                    vehicleRepository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecida: ${modelClass.name}"
        )
    }
}

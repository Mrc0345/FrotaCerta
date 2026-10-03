package com.marcus.frotacerta.ui.vehicle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcus.frotacerta.data.repository.VehicleRepository

class VehicleViewModelFactory(
    private val repository: VehicleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            return VehicleViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecida: ${modelClass.name}"
        )
    }
}

package com.marcus.frotacerta.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcus.frotacerta.data.remote.FrotaApiService
import com.marcus.frotacerta.data.repository.RentalRepository

class DashboardViewModelFactory(
    private val repository: RentalRepository,
    private val apiService: FrotaApiService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                DashboardViewModel::class.java
            )
        ) {

            return DashboardViewModel(
                repository = repository,
                apiService = apiService
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconhecida: ${modelClass.name}"
        )
    }
}

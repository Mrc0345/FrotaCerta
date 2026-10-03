package com.marcus.frotacerta.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcus.frotacerta.data.local.relation.RentalDetails
import com.marcus.frotacerta.data.repository.RentalRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    repository: RentalRepository
) : ViewModel() {

    val activeRentals: StateFlow<List<RentalDetails>> =
        repository.getActiveRentals()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}

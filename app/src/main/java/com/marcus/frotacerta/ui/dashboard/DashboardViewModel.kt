package com.marcus.frotacerta.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcus.frotacerta.data.local.relation.RentalDetails
import com.marcus.frotacerta.data.remote.FrotaApiService
import com.marcus.frotacerta.data.repository.RentalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SyncUiState {

    data object Idle : SyncUiState

    data object Loading : SyncUiState

    data class Success(
        val message: String
    ) : SyncUiState

    data class Error(
        val message: String
    ) : SyncUiState
}

class DashboardViewModel(
    repository: RentalRepository,
    private val apiService: FrotaApiService
) : ViewModel() {

    val activeRentals: StateFlow<List<RentalDetails>> =
        repository.getActiveRentals().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _syncState =
        MutableStateFlow<SyncUiState>(
            SyncUiState.Idle
        )

    val syncState: StateFlow<SyncUiState> =
        _syncState.asStateFlow()

    fun sync() {

        viewModelScope.launch {

            _syncState.value =
                SyncUiState.Loading

            try {

                val response =
                    apiService.getSyncStatus()

                _syncState.value =
                    SyncUiState.Success(
                        message =
                            "API conectada com sucesso. ID: ${response.id}"
                    )

            } catch (exception: Exception) {

                _syncState.value =
                    SyncUiState.Error(
                        message =
                            exception.message
                                ?: "Erro ao acessar a API."
                    )
            }
        }
    }
}

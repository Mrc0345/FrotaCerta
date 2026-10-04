package com.marcus.frotacerta.ui.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcus.frotacerta.data.local.relation.RentalDetails
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onOpenVehicles: () -> Unit,
    onNewRental: () -> Unit
) {

    val rentals by viewModel.activeRentals.collectAsStateWithLifecycle()
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("FrotaCerta")
                },
                actions = {
                    TextButton(
                        onClick = { viewModel.sync() },
                        enabled = syncState !is SyncUiState.Loading
                    ) {
                        Text(
                            when (syncState) {
                                is SyncUiState.Loading -> "Sincronizando..."
                                else -> "Sincronizar API"
                            }
                        )
                    }

                    TextButton(
                        onClick = onOpenVehicles
                    ) {
                        Text("Frota")
                    }
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewRental
            ) {
                Text("Nova locação")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (val state = syncState) {
                is SyncUiState.Success -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                is SyncUiState.Error -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = state.message,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                else -> {}
            }

            if (rentals.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Nenhuma locação ativa",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = "As locações em andamento aparecerão aqui.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 12.dp,
                        bottom = 90.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = rentals,
                        key = {
                            it.rental.id
                        }
                    ) { rental ->

                        RentalCard(
                            details = rental
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RentalCard(
    details: RentalDetails
) {

    val daysRemaining =
        calculateDaysRemaining(
            details.rental.expectedReturnDate
        )

    val delayed = daysRemaining < 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {

                    Text(
                        text = details.vehicle.model,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${details.vehicle.brand} • ${details.vehicle.plate}"
                    )
                }

                RentalStatus(
                    daysRemaining = daysRemaining
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Cliente: ${details.client.name}"
            )

            Text(
                text = "Telefone: ${details.client.phone}"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Saída: ${
                    formatDate(details.rental.startDate)
                }"
            )

            Text(
                text = "Entrega prevista: ${
                    formatDate(details.rental.expectedReturnDate)
                }"
            )

            Text(
                text = "Valor estimado: ${
                    formatCurrency(details.rental.estimatedTotal)
                }"
            )

            if (delayed) {
                Text(
                    text = "Locação em atraso",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun RentalStatus(
    daysRemaining: Long
) {

    val label = when {
        daysRemaining < 0 ->
            "${-daysRemaining} dia(s) em atraso"

        daysRemaining == 0L ->
            "Entrega hoje"

        else ->
            "$daysRemaining dia(s) restante(s)"
    }

    val backgroundColor =
        if (daysRemaining < 0) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.primaryContainer
        }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(50)
    ) {

        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            style = MaterialTheme.typography.labelMedium
        )
    }
}

private fun calculateDaysRemaining(
    expectedReturnDate: Long
): Long {
    val today = LocalDate.now()

    val returnDate = Instant
        .ofEpochMilli(expectedReturnDate)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return ChronoUnit.DAYS.between(
        today,
        returnDate
    )
}

private fun formatDate(
    timestamp: Long
): String {
    val date = Instant
        .ofEpochMilli(timestamp)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return "%02d/%02d/%04d".format(
        date.dayOfMonth,
        date.monthValue,
        date.year
    )
}

private fun formatCurrency(
    value: Double
): String {

    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

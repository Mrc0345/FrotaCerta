package com.marcus.frotacerta.ui.vehicle

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleListScreen(
    viewModel: VehicleViewModel,
    onAddVehicle: () -> Unit
) {

    val vehicles by viewModel.vehicles.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Minha Frota")
                }
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddVehicle
            ) {
                Text("Novo veículo")
            }
        }
    ) { innerPadding ->

        if (vehicles.isEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Nenhum veículo cadastrado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Cadastre o primeiro veículo da sua frota.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),

                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 90.dp
                ),

                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(
                    items = vehicles,
                    key = { vehicle ->
                        vehicle.id
                    }
                ) { vehicle ->

                    VehicleCard(
                        vehicle = vehicle
                    )
                }
            }
        }
    }
}

@Composable
private fun VehicleCard(
    vehicle: VehicleEntity
) {

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
                        text = vehicle.model,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = vehicle.brand,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                VehicleStatus(
                    status = vehicle.status
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Placa: ${vehicle.plate}"
            )

            Text(
                text = "Ano: ${vehicle.year}"
            )

            Text(
                text = "Diária: ${formatCurrency(vehicle.dailyRate)}"
            )
        }
    }
}

@Composable
private fun VehicleStatus(
    status: String
) {

    val label = when (status) {

        "DISPONIVEL" ->
            "DISPONÍVEL"

        "ALUGADO" ->
            "ALUGADO"

        "MANUTENCAO" ->
            "MANUTENÇÃO"

        else ->
            status
    }

    val backgroundColor = when (status) {

        "DISPONIVEL" ->
            MaterialTheme.colorScheme.primaryContainer

        "ALUGADO" ->
            MaterialTheme.colorScheme.secondaryContainer

        "MANUTENCAO" ->
            MaterialTheme.colorScheme.errorContainer

        else ->
            MaterialTheme.colorScheme.surfaceVariant
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

private fun formatCurrency(
    value: Double
): String {

    val brazilLocale = Locale(
        "pt",
        "BR"
    )

    return NumberFormat
        .getCurrencyInstance(brazilLocale)
        .format(value)
}

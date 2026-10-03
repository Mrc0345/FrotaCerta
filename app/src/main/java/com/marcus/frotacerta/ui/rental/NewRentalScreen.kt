package com.marcus.frotacerta.ui.rental

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import java.text.NumberFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRentalScreen(
    viewModel: RentalViewModel,
    contactId: Long,
    contactName: String,
    contactPhone: String,
    onSelectContact: () -> Unit,
    onRentalCreated: () -> Unit
) {

    val availableVehicles by
        viewModel.availableVehicles.collectAsStateWithLifecycle()

    var selectedVehicleId by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    var vehicleMenuExpanded by remember {
        mutableStateOf(false)
    }

    val selectedVehicle =
        availableVehicles.firstOrNull {
            it.id == selectedVehicleId
        }

    val today =
        LocalDate.now()

    var startDate by rememberSaveable {
        mutableStateOf(
            localDateToPickerMillis(today)
        )
    }

    var expectedReturnDate by rememberSaveable {
        mutableStateOf(
            localDateToPickerMillis(
                today.plusDays(1)
            )
        )
    }

    var showStartDatePicker by remember {
        mutableStateOf(false)
    }

    var showReturnDatePicker by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    var isSaving by remember {
        mutableStateOf(false)
    }

    val rentalDays =
        calculateRentalDays(
            startDate = startDate,
            expectedReturnDate = expectedReturnDate
        )

    val estimatedTotal =
        if (
            selectedVehicle != null &&
            rentalDays > 0
        ) {
            selectedVehicle.dailyRate * rentalDays
        } else {
            0.0
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nova locação")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Cliente",
                style =
                    MaterialTheme.typography.titleMedium
            )

            if (contactId <= 0 || contactName.isBlank()) {

                Text(
                    text = "Nenhum cliente selecionado."
                )

            } else {

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = contactName,
                            style =
                                MaterialTheme.typography
                                    .titleMedium
                        )

                        Text(
                            text = contactPhone
                        )
                    }
                }
            }

            OutlinedButton(
                onClick = onSelectContact,
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    if (contactName.isBlank()) {
                        "Selecionar cliente"
                    } else {
                        "Alterar cliente"
                    }
                )
            }

            Text(
                text = "Veículo",
                style =
                    MaterialTheme.typography.titleMedium
            )

            if (availableVehicles.isEmpty()) {

                Text(
                    text =
                        "Nenhum veículo disponível para locação."
                )

            } else {

                Box(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    OutlinedButton(
                        onClick = {
                            vehicleMenuExpanded = true
                        },
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text =
                                selectedVehicle?.let {
                                    "${it.brand} ${it.model} - ${it.plate}"
                                }
                                    ?: "Selecionar veículo"
                        )
                    }

                    DropdownMenu(
                        expanded =
                            vehicleMenuExpanded,
                        onDismissRequest = {
                            vehicleMenuExpanded = false
                        }
                    ) {

                        availableVehicles.forEach {
                            vehicle ->

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "${vehicle.brand} " +
                                            "${vehicle.model} - " +
                                            vehicle.plate
                                    )
                                },
                                onClick = {
                                    selectedVehicleId =
                                        vehicle.id

                                    vehicleMenuExpanded =
                                        false

                                    errorMessage = null
                                }
                            )
                        }
                    }
                }
            }

            Text(
                text = "Período",
                style =
                    MaterialTheme.typography.titleMedium
            )

            OutlinedButton(
                onClick = {
                    showStartDatePicker = true
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "Data de saída: ${
                            formatPickerDate(startDate)
                        }"
                )
            }

            OutlinedButton(
                onClick = {
                    showReturnDatePicker = true
                },
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    text =
                        "Entrega prevista: ${
                            formatPickerDate(
                                expectedReturnDate
                            )
                        }"
                )
            }

            if (expectedReturnDate < startDate) {

                Text(
                    text =
                        "A data de entrega não pode ser anterior à data de saída.",
                    color =
                        MaterialTheme.colorScheme.error
                )
            }

            selectedVehicle?.let { vehicle ->

                Card(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier =
                            Modifier.padding(16.dp),
                        verticalArrangement =
                            Arrangement.spacedBy(6.dp)
                    ) {

                        Text(
                            text = "Resumo da locação",
                            style =
                                MaterialTheme.typography
                                    .titleMedium
                        )

                        Text(
                            text =
                                "Diária: ${
                                    formatCurrency(
                                        vehicle.dailyRate
                                    )
                                }"
                        )

                        Text(
                            text =
                                "Período: $rentalDays dia(s)"
                        )

                        Text(
                            text =
                                "Total estimado: ${
                                    formatCurrency(
                                        estimatedTotal
                                    )
                                }",
                            style =
                                MaterialTheme.typography
                                    .titleMedium
                        )
                    }
                }
            }

            errorMessage?.let {

                Text(
                    text = it,
                    color =
                        MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {

                    errorMessage = null

                    when {

                        contactId <= 0 ||
                            contactName.isBlank() -> {

                            errorMessage =
                                "Selecione um cliente."
                        }

                        selectedVehicle == null -> {

                            errorMessage =
                                "Selecione um veículo disponível."
                        }

                        expectedReturnDate <
                            startDate -> {

                            errorMessage =
                                "Verifique as datas da locação."
                        }

                        rentalDays <= 0 -> {

                            errorMessage =
                                "O período da locação é inválido."
                        }

                        else -> {

                            isSaving = true

                            viewModel.createRental(
                                contactId = contactId,
                                clientName =
                                    contactName,
                                clientPhone =
                                    contactPhone,
                                vehicleId =
                                    selectedVehicle.id,
                                startDate =
                                    startDate,
                                expectedReturnDate =
                                    expectedReturnDate,
                                dailyRate =
                                    selectedVehicle.dailyRate,
                                estimatedTotal =
                                    estimatedTotal,

                                onSuccess = {
                                    isSaving = false
                                    onRentalCreated()
                                },

                                onError = {
                                    message ->

                                    isSaving = false
                                    errorMessage = message
                                }
                            )
                        }
                    }
                },

                enabled =
                    !isSaving &&
                        availableVehicles.isNotEmpty(),

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    if (isSaving) {
                        "Salvando..."
                    } else {
                        "Confirmar locação"
                    }
                )
            }
        }
    }

    if (showStartDatePicker) {

        RentalDatePickerDialog(
            initialDate = startDate,

            onDismiss = {
                showStartDatePicker = false
            },

            onDateSelected = { date ->

                startDate = date

                if (
                    expectedReturnDate <
                    startDate
                ) {
                    expectedReturnDate =
                        startDate
                }

                showStartDatePicker = false
            }
        )
    }

    if (showReturnDatePicker) {

        RentalDatePickerDialog(
            initialDate =
                expectedReturnDate,

            onDismiss = {
                showReturnDatePicker = false
            },

            onDateSelected = { date ->

                expectedReturnDate = date

                showReturnDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RentalDatePickerDialog(
    initialDate: Long,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {

    val datePickerState =
        androidx.compose.material3
            .rememberDatePickerState(
                initialSelectedDateMillis =
                    initialDate
            )

    DatePickerDialog(
        onDismissRequest = onDismiss,

        confirmButton = {

            TextButton(
                onClick = {

                    datePickerState
                        .selectedDateMillis
                        ?.let(onDateSelected)
                }
            ) {
                Text("Confirmar")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    ) {

        DatePicker(
            state = datePickerState
        )
    }
}

private fun calculateRentalDays(
    startDate: Long,
    expectedReturnDate: Long
): Long {

    val start =
        pickerMillisToLocalDate(
            startDate
        )

    val end =
        pickerMillisToLocalDate(
            expectedReturnDate
        )

    if (end.isBefore(start)) {
        return 0
    }

    val difference =
        ChronoUnit.DAYS.between(
            start,
            end
        )

    return if (difference == 0L) {
        1
    } else {
        difference
    }
}

private fun localDateToPickerMillis(
    date: LocalDate
): Long {

    return date
        .atStartOfDay(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}

private fun pickerMillisToLocalDate(
    millis: Long
): LocalDate {

    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
}

private fun formatPickerDate(
    millis: Long
): String {

    val date =
        pickerMillisToLocalDate(
            millis
        )

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

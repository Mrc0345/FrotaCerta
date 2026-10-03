package com.marcus.frotacerta.ui.vehicle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.marcus.frotacerta.data.local.entity.VehicleEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleFormScreen(
    viewModel: VehicleViewModel,
    onVehicleSaved: () -> Unit
) {

    var brand by rememberSaveable {
        mutableStateOf("")
    }

    var model by rememberSaveable {
        mutableStateOf("")
    }

    var plate by rememberSaveable {
        mutableStateOf("")
    }

    var year by rememberSaveable {
        mutableStateOf("")
    }

    var dailyRate by rememberSaveable {
        mutableStateOf("")
    }

    var brandError by rememberSaveable {
        mutableStateOf(false)
    }

    var modelError by rememberSaveable {
        mutableStateOf(false)
    }

    var plateError by rememberSaveable {
        mutableStateOf(false)
    }

    var yearError by rememberSaveable {
        mutableStateOf(false)
    }

    var dailyRateError by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Cadastrar veículo")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = brand,
                onValueChange = {
                    brand = it
                    brandError = false
                },
                label = {
                    Text("Marca")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = brandError,
                supportingText = {
                    if (brandError) {
                        Text("Informe a marca do veículo.")
                    }
                }
            )

            OutlinedTextField(
                value = model,
                onValueChange = {
                    model = it
                    modelError = false
                },
                label = {
                    Text("Modelo")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = modelError,
                supportingText = {
                    if (modelError) {
                        Text("Informe o modelo do veículo.")
                    }
                }
            )

            OutlinedTextField(
                value = plate,
                onValueChange = {
                    plate = it
                        .uppercase()
                        .replace(" ", "")

                    plateError = false
                },
                label = {
                    Text("Placa")
                },
                placeholder = {
                    Text("ABC-1234 ou ABC1D23")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = plateError,
                supportingText = {
                    if (plateError) {
                        Text(
                            "Use o formato ABC-1234 ou ABC1D23."
                        )
                    }
                }
            )

            OutlinedTextField(
                value = year,
                onValueChange = {
                    year = it.filter { char ->
                        char.isDigit()
                    }

                    yearError = false
                },
                label = {
                    Text("Ano")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                isError = yearError,
                supportingText = {
                    if (yearError) {
                        Text("Informe um ano válido.")
                    }
                }
            )

            OutlinedTextField(
                value = dailyRate,
                onValueChange = {
                    dailyRate = it.filter { char ->
                        char.isDigit() ||
                            char == ',' ||
                            char == '.'
                    }

                    dailyRateError = false
                },
                label = {
                    Text("Valor da diária")
                },
                placeholder = {
                    Text("Ex.: 150,00")
                },
                prefix = {
                    Text("R$ ")
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                isError = dailyRateError,
                supportingText = {
                    if (dailyRateError) {
                        Text(
                            "Informe uma diária maior que zero."
                        )
                    }
                }
            )

            Button(
                onClick = {

                    val normalizedPlate =
                        plate.trim().uppercase()

                    val oldPlateRegex =
                        Regex("^[A-Z]{3}-[0-9]{4}$")

                    val mercosulPlateRegex =
                        Regex("^[A-Z]{3}[0-9][A-Z][0-9]{2}$")

                    val parsedYear =
                        year.toIntOrNull()

                    val parsedDailyRate =
                        dailyRate
                            .replace(",", ".")
                            .toDoubleOrNull()

                    brandError =
                        brand.isBlank()

                    modelError =
                        model.isBlank()

                    plateError =
                        normalizedPlate.isBlank() ||
                        (
                            !oldPlateRegex.matches(normalizedPlate) &&
                            !mercosulPlateRegex.matches(normalizedPlate)
                        )

                    yearError =
                        parsedYear == null ||
                        parsedYear <= 0

                    dailyRateError =
                        parsedDailyRate == null ||
                        parsedDailyRate <= 0

                    val formIsValid =
                        !brandError &&
                        !modelError &&
                        !plateError &&
                        !yearError &&
                        !dailyRateError

                    if (formIsValid) {

                        val vehicle = VehicleEntity(
                            brand = brand.trim(),
                            model = model.trim(),
                            plate = normalizedPlate,
                            year = parsedYear!!,
                            dailyRate = parsedDailyRate!!
                        )

                        viewModel.insertVehicle(vehicle)

                        onVehicleSaved()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cadastrar veículo")
            }
        }
    }
}

package com.marcus.frotacerta.ui.rental

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRentalScreen(
    contactName: String,
    contactPhone: String,
    onSelectContact: () -> Unit
) {

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
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Cliente",
                style = MaterialTheme.typography.titleMedium
            )

            if (contactName.isBlank()) {

                Text(
                    text = "Nenhum cliente selecionado."
                )

            } else {

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = contactName,
                            style =
                                MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = contactPhone
                        )
                    }
                }
            }

            Button(
                onClick = onSelectContact,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = if (contactName.isBlank()) {
                        "Selecionar cliente"
                    } else {
                        "Alterar cliente"
                    }
                )
            }
        }
    }
}

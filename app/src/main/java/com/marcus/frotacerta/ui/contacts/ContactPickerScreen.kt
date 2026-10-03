package com.marcus.frotacerta.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.marcus.frotacerta.data.contacts.ContactsDataSource
import com.marcus.frotacerta.domain.model.ContactModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactPickerScreen(
    onContactSelected: (ContactModel) -> Unit
) {

    val context = LocalContext.current

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var permissionDenied by remember {
        mutableStateOf(false)
    }

    var contacts by remember {
        mutableStateOf<List<ContactModel>>(emptyList())
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            permissionGranted = granted
            permissionDenied = !granted
        }

    LaunchedEffect(Unit) {

        if (!permissionGranted) {
            permissionLauncher.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    LaunchedEffect(permissionGranted) {

        if (permissionGranted) {

            isLoading = true
            errorMessage = null

            try {

                contacts = withContext(Dispatchers.IO) {

                    ContactsDataSource(
                        context.contentResolver
                    ).getContacts()
                }

            } catch (exception: Exception) {

                errorMessage =
                    "Não foi possível carregar os contatos."

            } finally {

                isLoading = false
            }
        }
    }

    val filteredContacts = remember(
        contacts,
        searchQuery
    ) {

        if (searchQuery.isBlank()) {

            contacts

        } else {

            contacts.filter { contact ->

                contact.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Selecionar cliente")
                }
            )
        }
    ) { innerPadding ->

        when {

            permissionDenied && !permissionGranted -> {

                PermissionDeniedContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),

                    onTryAgain = {

                        permissionLauncher.launch(
                            Manifest.permission.READ_CONTACTS
                        )
                    }
                )
            }

            isLoading -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Text(
                        text = "Carregando contatos...",
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }

            errorMessage != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = errorMessage ?: "Erro desconhecido.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            permissionGranted -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        label = {
                            Text("Buscar por nome")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true
                    )

                    if (filteredContacts.isEmpty()) {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = if (searchQuery.isBlank()) {
                                    "Nenhum contato encontrado."
                                } else {
                                    "Nenhum contato corresponde à busca."
                                }
                            )
                        }

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                            verticalArrangement =
                                Arrangement.spacedBy(8.dp)
                        ) {

                            items(
                                items = filteredContacts,
                                key = { contact ->
                                    "${contact.id}-${contact.phone}"
                                }
                            ) { contact ->

                                ContactItem(
                                    contact = contact,
                                    onClick = {
                                        onContactSelected(contact)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionDeniedContent(
    modifier: Modifier = Modifier,
    onTryAgain: () -> Unit
) {

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Permissão necessária",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "O FrotaCerta precisa acessar seus contatos para selecionar o cliente da locação.",
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 16.dp
            )
        )

        Button(
            onClick = onTryAgain
        ) {
            Text("Tentar novamente")
        }
    }
}

@Composable
private fun ContactItem(
    contact: ContactModel,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = contact.phone,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

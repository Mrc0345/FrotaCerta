package com.marcus.frotacerta.data.di

import android.content.Context
import com.marcus.frotacerta.data.local.DatabaseProvider
import com.marcus.frotacerta.data.repository.ClientRepository
import com.marcus.frotacerta.data.repository.RentalRepository
import com.marcus.frotacerta.data.repository.VehicleRepository

class AppContainer(
    context: Context
) {

    private val database =
        DatabaseProvider.getDatabase(context)

    val vehicleRepository =
        VehicleRepository(
            database.vehicleDao()
        )

    val clientRepository =
        ClientRepository(
            database.clientDao()
        )

    val rentalRepository =
        RentalRepository(
            database = database,
            rentalDao = database.rentalDao(),
            clientDao = database.clientDao(),
            vehicleDao = database.vehicleDao()
        )
}

package com.marcus.frotacerta.data.di

import android.content.Context
import com.marcus.frotacerta.data.local.DatabaseProvider
import com.marcus.frotacerta.data.repository.ClientRepository
import com.marcus.frotacerta.data.repository.RentalRepository
import com.marcus.frotacerta.data.repository.VehicleRepository

class AppContainer(context: Context) {

    private val database by lazy {
        DatabaseProvider.getDatabase(context)
    }

    val vehicleRepository: VehicleRepository by lazy {
        VehicleRepository(database.vehicleDao())
    }

    val clientRepository: ClientRepository by lazy {
        ClientRepository(database.clientDao())
    }

    val rentalRepository: RentalRepository by lazy {
        RentalRepository(database.rentalDao())
    }
}

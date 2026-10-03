package com.marcus.frotacerta.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.marcus.frotacerta.data.local.dao.ClientDao
import com.marcus.frotacerta.data.local.dao.RentalDao
import com.marcus.frotacerta.data.local.dao.VehicleDao
import com.marcus.frotacerta.data.local.entity.ClientEntity
import com.marcus.frotacerta.data.local.entity.RentalEntity
import com.marcus.frotacerta.data.local.entity.VehicleEntity

@Database(
    entities = [
        VehicleEntity::class,
        ClientEntity::class,
        RentalEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    abstract fun clientDao(): ClientDao

    abstract fun rentalDao(): RentalDao
}

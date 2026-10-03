package com.marcus.frotacerta.data.repository

import androidx.room3.withWriteTransaction
import com.marcus.frotacerta.data.local.AppDatabase
import com.marcus.frotacerta.data.local.dao.ClientDao
import com.marcus.frotacerta.data.local.dao.RentalDao
import com.marcus.frotacerta.data.local.dao.VehicleDao
import com.marcus.frotacerta.data.local.entity.ClientEntity
import com.marcus.frotacerta.data.local.entity.RentalEntity
import com.marcus.frotacerta.data.local.relation.RentalDetails
import kotlinx.coroutines.flow.Flow

class RentalRepository(
    private val database: AppDatabase,
    private val rentalDao: RentalDao,
    private val clientDao: ClientDao,
    private val vehicleDao: VehicleDao
) {

    fun getActiveRentals(): Flow<List<RentalDetails>> {
        return rentalDao.getActiveRentals()
    }

    fun getAllRentals(): Flow<List<RentalDetails>> {
        return rentalDao.getAllRentals()
    }

    suspend fun getRentalById(
        rentalId: Long
    ): RentalDetails? {
        return rentalDao.getById(rentalId)
    }

    suspend fun createRental(
        contactId: Long,
        clientName: String,
        clientPhone: String,
        vehicleId: Long,
        startDate: Long,
        expectedReturnDate: Long,
        dailyRate: Double,
        estimatedTotal: Double
    ): Long {

        return database.withWriteTransaction {

            val existingClient =
                clientDao.getByContactId(contactId)

            val clientId =
                if (existingClient != null) {

                    existingClient.id

                } else {

                    clientDao.insert(
                        ClientEntity(
                            contactId = contactId,
                            name = clientName,
                            phone = clientPhone
                        )
                    )
                }

            val rentalId =
                rentalDao.insert(
                    RentalEntity(
                        vehicleId = vehicleId,
                        clientId = clientId,
                        startDate = startDate,
                        expectedReturnDate = expectedReturnDate,
                        dailyRate = dailyRate,
                        estimatedTotal = estimatedTotal,
                        status = "ATIVA"
                    )
                )

            vehicleDao.updateStatus(
                vehicleId = vehicleId,
                status = "ALUGADO"
            )

            rentalId
        }
    }

    suspend fun updateRentalStatus(
        rentalId: Long,
        status: String
    ) {
        rentalDao.updateStatus(
            rentalId = rentalId,
            status = status
        )
    }
}

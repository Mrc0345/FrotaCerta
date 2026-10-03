package com.marcus.frotacerta.data.repository

import com.marcus.frotacerta.data.local.dao.RentalDao
import com.marcus.frotacerta.data.local.entity.RentalEntity
import com.marcus.frotacerta.data.local.relation.RentalDetails
import kotlinx.coroutines.flow.Flow

class RentalRepository(
    private val rentalDao: RentalDao
) {

    fun getActiveRentals(): Flow<List<RentalDetails>> {
        return rentalDao.getActiveRentals()
    }

    fun getAllRentals(): Flow<List<RentalDetails>> {
        return rentalDao.getAllRentals()
    }

    suspend fun getRentalById(rentalId: Long): RentalDetails? {
        return rentalDao.getById(rentalId)
    }

    suspend fun insertRental(rental: RentalEntity): Long {
        return rentalDao.insert(rental)
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

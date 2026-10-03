package com.marcus.frotacerta.data.repository

import com.marcus.frotacerta.data.local.dao.VehicleDao
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

class VehicleRepository(
    private val vehicleDao: VehicleDao
) {

    fun getAllVehicles(): Flow<List<VehicleEntity>> {
        return vehicleDao.getAll()
    }

    fun getAvailableVehicles(): Flow<List<VehicleEntity>> {
        return vehicleDao.getAvailable()
    }

    suspend fun getVehicleById(id: Long): VehicleEntity? {
        return vehicleDao.getById(id)
    }

    suspend fun insertVehicle(vehicle: VehicleEntity) {
        vehicleDao.insert(vehicle)
    }

    suspend fun updateVehicle(vehicle: VehicleEntity) {
        vehicleDao.update(vehicle)
    }

    suspend fun updateVehicleStatus(
        vehicleId: Long,
        status: String
    ) {
        vehicleDao.updateStatus(
            vehicleId = vehicleId,
            status = status
        )
    }
}

package com.marcus.frotacerta.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.marcus.frotacerta.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(vehicle: VehicleEntity)

    @Update
    suspend fun update(vehicle: VehicleEntity)

    @Query(
        """
        SELECT * FROM vehicles
        ORDER BY brand ASC, model ASC
        """
    )
    fun getAll(): Flow<List<VehicleEntity>>

    @Query(
        """
        SELECT * FROM vehicles
        WHERE status = 'DISPONIVEL'
        ORDER BY brand ASC, model ASC
        """
    )
    fun getAvailable(): Flow<List<VehicleEntity>>

    @Query(
        """
        SELECT * FROM vehicles
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: Long): VehicleEntity?

    @Query(
        """
        UPDATE vehicles
        SET status = :status
        WHERE id = :vehicleId
        """
    )
    suspend fun updateStatus(
        vehicleId: Long,
        status: String
    )
}

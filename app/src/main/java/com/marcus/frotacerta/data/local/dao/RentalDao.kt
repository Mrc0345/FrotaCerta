package com.marcus.frotacerta.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import com.marcus.frotacerta.data.local.entity.RentalEntity
import com.marcus.frotacerta.data.local.relation.RentalDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(rental: RentalEntity): Long

    @Transaction
    @Query(
        """
        SELECT * FROM rentals
        WHERE status = 'ATIVA'
        ORDER BY expectedReturnDate ASC
        """
    )
    fun getActiveRentals(): Flow<List<RentalDetails>>

    @Transaction
    @Query(
        """
        SELECT * FROM rentals
        ORDER BY startDate DESC
        """
    )
    fun getAllRentals(): Flow<List<RentalDetails>>

    @Transaction
    @Query(
        """
        SELECT * FROM rentals
        WHERE id = :rentalId
        LIMIT 1
        """
    )
    suspend fun getById(rentalId: Long): RentalDetails?

    @Query(
        """
        UPDATE rentals
        SET status = :status
        WHERE id = :rentalId
        """
    )
    suspend fun updateStatus(
        rentalId: Long,
        status: String
    )
}

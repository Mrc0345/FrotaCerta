package com.marcus.frotacerta.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import com.marcus.frotacerta.data.local.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(client: ClientEntity): Long

    @Query(
        """
        SELECT * FROM clients
        ORDER BY name ASC
        """
    )
    fun getAll(): Flow<List<ClientEntity>>

    @Query(
        """
        SELECT * FROM clients
        WHERE id = :id
        LIMIT 1
        """
    )
    suspend fun getById(id: Long): ClientEntity?

    @Query(
        """
        SELECT * FROM clients
        WHERE contactId = :contactId
        LIMIT 1
        """
    )
    suspend fun getByContactId(contactId: Long): ClientEntity?
}

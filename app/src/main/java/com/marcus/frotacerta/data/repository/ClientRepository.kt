package com.marcus.frotacerta.data.repository

import com.marcus.frotacerta.data.local.dao.ClientDao
import com.marcus.frotacerta.data.local.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

class ClientRepository(
    private val clientDao: ClientDao
) {

    fun getAllClients(): Flow<List<ClientEntity>> {
        return clientDao.getAll()
    }

    suspend fun getClientById(id: Long): ClientEntity? {
        return clientDao.getById(id)
    }

    suspend fun getClientByContactId(contactId: Long): ClientEntity? {
        return clientDao.getByContactId(contactId)
    }

    suspend fun insertClient(client: ClientEntity): Long {
        return clientDao.insert(client)
    }
}

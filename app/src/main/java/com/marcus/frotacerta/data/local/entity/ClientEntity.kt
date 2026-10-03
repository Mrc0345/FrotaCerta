package com.marcus.frotacerta.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "clients")
data class ClientEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val contactId: Long,

    val name: String,

    val phone: String
)
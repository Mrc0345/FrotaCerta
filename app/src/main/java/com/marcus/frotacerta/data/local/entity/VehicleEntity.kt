package com.marcus.frotacerta.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "vehicles")
data class VehicleEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val brand: String,

    val model: String,

    val plate: String,

    val year: Int,

    val dailyRate: Double,

    val status: String = "DISPONIVEL"
)


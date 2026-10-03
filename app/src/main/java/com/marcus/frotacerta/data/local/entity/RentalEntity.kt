package com.marcus.frotacerta.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "rentals",
    foreignKeys = [
        ForeignKey(
            entity = VehicleEntity::class,
            parentColumns = ["id"],
            childColumns = ["vehicleId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = ClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["vehicleId"]),
        Index(value = ["clientId"])
    ]
)
data class RentalEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val vehicleId: Long,

    val clientId: Long,

    val startDate: Long,

    val expectedReturnDate: Long,

    val dailyRate: Double,

    val estimatedTotal: Double,

    val status: String = "ATIVA"
)


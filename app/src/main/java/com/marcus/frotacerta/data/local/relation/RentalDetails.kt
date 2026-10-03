package com.marcus.frotacerta.data.local.relation

import androidx.room3.Embedded
import androidx.room3.Relation
import com.marcus.frotacerta.data.local.entity.ClientEntity
import com.marcus.frotacerta.data.local.entity.RentalEntity
import com.marcus.frotacerta.data.local.entity.VehicleEntity

data class RentalDetails(

    @Embedded
    val rental: RentalEntity,

    @Relation(
        parentColumns = ["vehicleId"],
        entityColumns = ["id"]
    )
    val vehicle: VehicleEntity,

    @Relation(
        parentColumns = ["clientId"],
        entityColumns = ["id"]
    )
    val client: ClientEntity
)

package com.marcus.frotacerta.data.remote.dto

data class RentalDto(
    val id: Long,
    val vehicleId: Long,
    val clientId: Long,
    val startDate: Long,
    val expectedReturnDate: Long,
    val dailyRate: Double,
    val estimatedTotal: Double,
    val status: String
)

package com.marcus.frotacerta.data.remote.dto

data class VehicleDto(
    val id: Long,
    val brand: String,
    val model: String,
    val plate: String,
    val year: Int,
    val dailyRate: Double,
    val status: String
)

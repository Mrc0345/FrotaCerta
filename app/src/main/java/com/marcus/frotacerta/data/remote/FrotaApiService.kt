package com.marcus.frotacerta.data.remote

import com.marcus.frotacerta.data.remote.dto.RentalDto
import com.marcus.frotacerta.data.remote.dto.SyncStatusDto
import com.marcus.frotacerta.data.remote.dto.VehicleDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FrotaApiService {

    @GET("vehicles")
    suspend fun getVehicles(): List<VehicleDto>

    @POST("vehicles")
    suspend fun sendVehicle(
        @Body vehicle: VehicleDto
    ): VehicleDto

    @GET("rentals")
    suspend fun getRentals(): List<RentalDto>

    @POST("rentals")
    suspend fun sendRental(
        @Body rental: RentalDto
    ): RentalDto

    @GET("posts/1")
    suspend fun getSyncStatus(): SyncStatusDto
}

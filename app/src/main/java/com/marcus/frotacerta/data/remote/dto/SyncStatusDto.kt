package com.marcus.frotacerta.data.remote.dto

data class SyncStatusDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

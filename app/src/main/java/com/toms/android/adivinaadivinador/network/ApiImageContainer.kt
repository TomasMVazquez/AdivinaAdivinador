package com.toms.android.adivinaadivinador.network

data class ApiImageContainer (
        val total: Double,
        val totalHits: Double,
        val hits: List<ApiImage> = listOf<ApiImage>()
)
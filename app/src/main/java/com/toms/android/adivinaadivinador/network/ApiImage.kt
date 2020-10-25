package com.toms.android.adivinaadivinador.network

import com.squareup.moshi.Json

data class ApiImage(
        val id: Double,
        val pageURL: String,
        val type: String,
        val tags: String,
        @Json(name = "previewURL") val imgSrcUrl: String,
        val previewWidth: Double,
        val previewHeight: Double,
        val webformatURL: String,
        val webformatWidth: Double,
        val webformatHeight: Double,
        val largeImageURL: String,
        val imageWidth: Double,
        val imageHeight: Double,
        val imageSize: Double,
        val views: Double,
        val downloads: Double,
        val favorites: Double,
        val likes: Double,
        val comments: Double,
        val user_id: Double,
        val user: String,
        val userImageURL: String
)
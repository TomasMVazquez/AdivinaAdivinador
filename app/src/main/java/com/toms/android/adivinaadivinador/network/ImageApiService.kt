package com.toms.android.adivinaadivinador.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://pixabay.com/api/"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface ImagesApiService {
    @GET(".") //to declare that your final URL is the same as your base URL.
    fun getAnimals(@Query("key")key: String,
                   @Query("q")search: String,
                   @Query("lang")lang: String,
                   @Query("image_type")type: String,
                   @Query("orientation")orientation: String,
                   @Query("category")category: String,
                   @Query("colors")colors: String):
            Call<String>
}

object ImageApi {
    val retrofitService : ImagesApiService by lazy {
        retrofit.create(ImagesApiService::class.java)
    }
}
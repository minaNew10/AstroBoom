package com.minabeshara.astroboom.api

import com.minabeshara.astroboom.model.PictureOfDay
import com.minabeshara.astroboom.utils.Constants.BASE_URL
import com.minabeshara.astroboom.utils.QueryConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface NasaApiService{
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("api_key") api_key :String,
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
    ) : String

    @GET("planetary/apod")
    suspend fun getImageOfDay(
        @Query("api_key") api_key :String
    ) : PictureOfDay
}

object NasaApi {
    val retrofitService : NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java) }
}
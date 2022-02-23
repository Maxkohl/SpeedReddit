package com.maxkohl.speedreddit.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://reddit.com/r/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
private val retrofit =
    Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
        BASE_URL
    ).build()

interface RedditApiService {
    @GET("all.json?limit=40")
    suspend fun getAllResponses(): RedditApiResponse
}

object RedditApi {
    val retrofitService: RedditApiService by lazy {
        retrofit.create(RedditApiService::class.java)
    }
}

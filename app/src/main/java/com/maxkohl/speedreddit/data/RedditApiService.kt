package com.maxkohl.speedreddit.data

import retrofit2.http.GET

interface RedditApiService {
    @GET("all.json?limit=100")
    suspend fun getAllResponses(): RedditApiResponse
}


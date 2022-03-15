package com.maxkohl.speedreddit.data

import com.maxkohl.speedreddit.utils.ApiBaseTest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class RedditApiServiceTestApi : ApiBaseTest() {
    private lateinit var service: RedditApiService

    @Before
    fun setup() {
        val url = mockWebServer.url("/")
        service = Retrofit.Builder().baseUrl(url).addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        ).build().create(RedditApiService::class.java)
    }

    @Test
    fun api_service() {
        enqueue("reddit_api_response.json")
        runBlocking {
            val apiResponse: RedditApiResponse = service.getAllResponses()

            assertNotNull(apiResponse)
            assertThat(apiResponse.data.children.size, `is`(4))
        }
    }
}
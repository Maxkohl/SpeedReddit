package com.maxkohl.speedreddit.data

import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MainRepositoryTest : TestCase() {

    private lateinit var remoteDataSource: FakeDataSource

    private lateinit var mainRepository: MainRepository

    private val mockRedditApiResponse = RedditApiResponse(
        RedditDataResponse(
            listOf(
                RedditChildrenResponse(
                    RedditPost(
                        "movie_post",
                        "test_subreddit",
                        "new",
                        "preview_image_source",
                        "test_url",
                        false,
                        false,
                        "movie_media_type",
                        "test_text",
                        null
                    )
                ),
                RedditChildrenResponse(
                    RedditPost(
                        "image_post",
                        "test_subreddit",
                        "new",
                        "preview_image_source",
                        "test_url",
                        false,
                        false,
                        "image_media_type",
                        "test_text",
                        null
                    )
                )
            ), null, null
        )
    )

    @Before
    fun createRepository() {
        remoteDataSource = FakeDataSource(mockRedditApiResponse)
        mainRepository = MainRepository(remoteDataSource)
    }

    @Test
    fun testGetRedditPostsData() {
        runBlocking {
            val result = mainRepository.getRedditPostsData()

            assertEquals(mockRedditApiResponse, result)
        }
    }
}
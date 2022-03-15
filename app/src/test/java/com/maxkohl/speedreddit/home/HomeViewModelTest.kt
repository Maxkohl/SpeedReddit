package com.maxkohl.speedreddit.home

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.maxkohl.speedreddit.data.*
import com.maxkohl.speedreddit.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = Application::class)
class HomeViewModelTest {
    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var apiService: RedditApiService

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
        mainRepository = MainRepository(apiService)
        homeViewModel = HomeViewModel(mainRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getRedditPostsList() successfully fetches redditPostAPI response from main repository`() {
        val mockRedditApiResponse = RedditApiResponse(
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

        runBlocking {
            Mockito.`when`(mainRepository.getRedditPostsData())
                .thenReturn(mockRedditApiResponse)

            homeViewModel.getRedditPostsList()
            val result = homeViewModel.redditPostsLists.getOrAwaitValue()

            assertEquals(
                mockRedditApiResponse.data.children.size, result.size
            )
            assertEquals(
                mockRedditApiResponse.data.children[0].data, result[0]
            )
        }
    }

    @Test
    fun `getRedditPostsLists() sets appropriate response value on success`() {
        val mockRedditApiResponse = RedditApiResponse(
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

        runBlocking {
            Mockito.`when`(mainRepository.getRedditPostsData())
                .thenReturn(mockRedditApiResponse)

            homeViewModel.getRedditPostsList()
            val result = homeViewModel.redditPostsLists.getOrAwaitValue()

            assertEquals("${result[0]} RECEIVED", homeViewModel.response.value)
        }
    }

    @Test
    fun `getRedditPostsLists() sets appropriate response when exception occurs`() {
        runBlocking {
            homeViewModel.getRedditPostsList()

            assertEquals("Failure: null", homeViewModel.response.value)
        }
    }
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    try {
        afterObserve.invoke()
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}
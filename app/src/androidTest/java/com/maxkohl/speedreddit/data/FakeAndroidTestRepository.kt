package com.maxkohl.speedreddit.data

import javax.inject.Inject

class FakeAndroidTestRepository @Inject constructor() : RepositoryInterface {
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

    override suspend fun getRedditPostsData(): RedditApiResponse {
        return mockRedditApiResponse
    }
}
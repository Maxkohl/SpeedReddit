package com.maxkohl.speedreddit.data

import javax.inject.Inject

class FakeAndroidTestRepository @Inject constructor() : RepositoryInterface {
    private val mockRedditApiResponse = RedditApiResponse(
        RedditDataResponse(
            populateRedditPosts(), null, null
        )
    )

    override suspend fun getRedditPostsData(): RedditApiResponse {
        return mockRedditApiResponse
    }

    private fun populateRedditPosts(): MutableList<RedditChildrenResponse> {
        val redditResponseList: MutableList<RedditChildrenResponse> = mutableListOf()

        for (i in 0..3) {
            redditResponseList.add(
                RedditChildrenResponse(
                    RedditPost(
                        "image_post_${i}",
                        "test_subreddit",
                        "new",
                        "preview_image_source",
                        "",
                        false,
                        false,
                        "image",
                        "image_test_text",
                        null
                    )
                )
            )
            redditResponseList.add(
                RedditChildrenResponse(
                    RedditPost(
                        "video_post_${i}",
                        "test_subreddit",
                        "new",
                        "preview_image_source",
                        "",
                        false,
                        false,
                        "hosted:video",
                        "vide_test_text",
                        null
                    )
                )
            )
            redditResponseList.add(
                RedditChildrenResponse(
                    RedditPost(
                        "text_post_${i}",
                        "test_subreddit",
                        "new",
                        "",
                        "",
                        false,
                        false,
                        "text",
                        "Test text for android UI testing purposes",
                        null
                    )
                )
            )

            redditResponseList.add(
                RedditChildrenResponse(
                    RedditPost(
                        "link_post_${i}",
                        "test_subreddit",
                        "new",
                        "preview_image_source",
                        "link_test_url",
                        false,
                        false,
                        "image_link_type",
                        "test_text",
                        null
                    )
                )
            )
        }
        return redditResponseList
    }
}
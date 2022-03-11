package com.maxkohl.speedreddit.data

import javax.inject.Inject
import javax.inject.Named


class MainRepository @Inject constructor(@Named("RedditApiService") private var redditApi: RedditApiService) {

    suspend fun getRedditPostsData(): RedditApiResponse  {
        return redditApi.getAllResponses()
    }

}
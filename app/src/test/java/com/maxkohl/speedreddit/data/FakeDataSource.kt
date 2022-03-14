package com.maxkohl.speedreddit.data

class FakeDataSource(var redditApiResponse: RedditApiResponse): RedditApiService {
    override suspend fun getAllResponses(): RedditApiResponse {
        return redditApiResponse
    }
}
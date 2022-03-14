package com.maxkohl.speedreddit.data

interface RepositoryInterface {
    suspend fun getRedditPostsData(): RedditApiResponse
}
package com.maxkohl.speedreddit.data

import com.squareup.moshi.Json

data class RedditApiResponse(val data: RedditDataResponse)

data class RedditDataResponse(
    val children: List<RedditChildrenResponse>,
    val after: String?,
    val before: String?
)

data class RedditChildrenResponse(val data: RedditPost)

data class RedditPost(
    val title: String,
    @Json(name = "subreddit_name_prefixed") val subreddit: String,
    @Json(name = "score") val upvoteCount: Int,
    @Json(name = "thumbnail") val previewImgSrc: String,
    @Json(name = "url") val contentUrl: String
)
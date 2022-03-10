package com.maxkohl.speedreddit.data

import com.squareup.moshi.Json
import java.io.Serializable

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
    @Json(name = "score") val upvoteCount: String,
    @Json(name = "thumbnail") val previewImgSrc: String,
    @Json(name = "url") val contentUrl: String,
    @Json(name = "is_video") val isVideo: Boolean,
    @Json(name = "is_self") val isSelfPost: Boolean,
    @Json(name = "post_hint") val mediaType: String?,
    @Json(name = "selftext") val postText: String?,
    val media: RedditMedia?
) : Serializable

data class RedditMedia(
    @Json(name = "reddit_video") val redditVideo: RedditVideo?,
    @Json(name = "oembed") val richVideo: RichVideo?
    ) : Serializable

data class RedditVideo(
    @Json(name = "fallback_url") val videoSrc: String?
    ) : Serializable

data class RichVideo(
    @Json(name = "thumbnail_url") val videoSrc: String?
) : Serializable
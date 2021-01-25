package com.mikewellback.redditgallery.api

data class RedditTopPosts(val data: RedditData)

data class RedditData(val children: Array<RedditChild>)

data class RedditChild(val data: RedditChildData)

data class RedditChildData(
    val subreddit: String,
    val is_gallery: Boolean?,
    val title: String,
    val downs: Int,
    val upvote_ratio: Float,
    val ups: Int,
    val score: Int,
    val created: Long,
    val selftext_html: String?,
    val preview: RedditImages?,
    val author: String,
    val num_comments: Int,
    val permalink: String,
    val created_utc: Long,
    val is_video: Boolean?
)

data class RedditImages(val images: Array<RedditImage>)

data class RedditImage(
    val source: RedditImageData,
    val resolutions: Array<RedditImageData>
)

data class RedditImageData(
    val url: String,
    val width: Int,
    val height: Int
)

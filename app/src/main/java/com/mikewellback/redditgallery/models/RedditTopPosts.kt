package com.mikewellback.redditgallery.models

import androidx.core.text.HtmlCompat
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RedditTopPosts(val data: RedditData)

data class RedditData(val children: Array<RedditChild>)

data class RedditChild(val data: RedditChildData)

@Entity(tableName = "favorites")
data class RedditChildData(
    @PrimaryKey val name: String,
    val subreddit: String,
    val is_gallery: Boolean? = false,
    val title: String,
    val downs: Int,
    val upvote_ratio: Float,
    val ups: Int,
    val score: Int,
    val created: Long,
    val selftext_html: String? = "",
    val preview: RedditImages?,
    val author: String,
    val num_comments: Int,
    val permalink: String,
    val created_utc: Long,
    val is_video: Boolean? = false
)

data class RedditImages(val images: Array<RedditImage>) {
    fun sourceUrl(): String? = images.firstOrNull()?.source?.url
    fun escapedUrl(): String = HtmlCompat.fromHtml(sourceUrl() ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
}

data class RedditImage(
    val source: RedditImageData,
    val resolutions: Array<RedditImageData>
)

data class RedditImageData(
    val url: String,
    val width: Int,
    val height: Int
)

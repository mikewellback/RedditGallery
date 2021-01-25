package com.mikewellback.redditgallery.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RedditAPI {
    @GET("r/{topic}/top.json")
    fun getTopPosts(@Path("topic") topic: String): Call<RedditTopPosts>
}
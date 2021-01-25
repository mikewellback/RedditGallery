package com.mikewellback.redditgallery.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RedditRetrofit {
    companion object {
        val service: RedditAPI by lazy {
            Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RedditAPI::class.java)
        }
    }
}
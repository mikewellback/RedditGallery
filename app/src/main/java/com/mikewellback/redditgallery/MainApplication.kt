package com.mikewellback.redditgallery

import android.app.Application
import com.mikewellback.redditgallery.db.RedditDatabase

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        RedditDatabase.initDB(this)
    }

}
package com.mikewellback.redditgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.mikewellback.redditgallery.api.RedditRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        rv.adapter = RedditAdapter()

        GlobalScope.launch(Dispatchers.IO) {
            val posts = RedditRetrofit.service.getTopPosts("photography").execute()
            if (posts.isSuccessful) {
                val body = posts.body()
                if (body != null) {
                    val childData = body.data.children.filter {
                        it.data.preview != null && it.data.preview.images.isNotEmpty()
                    }.map {
                        it.data
                    }
                    GlobalScope.launch(Dispatchers.Main) {
                        (rv.adapter as? RedditAdapter)?.elements = childData
                    }
                } else {
                    GlobalScope.launch(Dispatchers.Main) {

                    }
                }
            }
        }
    }
}
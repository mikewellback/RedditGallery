package com.mikewellback.redditgallery.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikewellback.redditgallery.api.RedditChildData

class FavoritesViewModel: ViewModel() {

    private val _posts = MutableLiveData<List<RedditChildData>>().apply {
        value = listOf()
    }
    val posts: LiveData<List<RedditChildData>> = _posts

}
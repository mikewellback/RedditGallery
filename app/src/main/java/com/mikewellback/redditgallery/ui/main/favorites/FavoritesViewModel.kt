package com.mikewellback.redditgallery.ui.main.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikewellback.redditgallery.db.RedditDatabase
import com.mikewellback.redditgallery.models.RedditChildData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

class FavoritesViewModel: ViewModel() {

    private val _favorites = MutableLiveData<List<RedditChildData>?>().apply {
        value = null
    }
    val favorites: LiveData<List<RedditChildData>?> = _favorites

    fun fetchDatabaseData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().getAll().collect { favorites ->
                launch(Dispatchers.Main) { _favorites.value = favorites }
            }
        }
    }

    fun addFavoriteItem(context: Context, post: RedditChildData) {
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().insertAll(post)
        }
    }

    fun removeFavoriteItem(context: Context, post: RedditChildData) {
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().delete(post)
        }
    }

}
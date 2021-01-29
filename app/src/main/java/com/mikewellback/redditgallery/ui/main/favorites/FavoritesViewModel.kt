package com.mikewellback.redditgallery.ui.main.favorites

import android.content.Context
import android.os.Handler
import android.os.Looper
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

    private val _removed = MutableLiveData<List<RedditChildData>>().apply {
        value = listOf()
    }
    val removed: LiveData<List<RedditChildData>> = _removed

    fun fetchDatabaseData(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().getAll().collect { favorites ->
                launch(Dispatchers.Main) { _favorites.value = favorites }
            }
        }
    }

    fun addFavoriteItem(context: Context, post: RedditChildData) {
        post.created = System.currentTimeMillis()
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().insertAll(post)
        }
    }

    val handler: Handler by lazy { Handler(Looper.getMainLooper()) }
    val trashRunable = Runnable { _removed.value = listOf() }

    fun removeFavoriteItem(context: Context, post: RedditChildData, undoTime: Long) {
        if (_removed.value?.contains(post) != true) {
            _removed.value = listOf(post, *_removed.value!!.toTypedArray())
        }
        handler.removeCallbacks(trashRunable)
        handler.postDelayed(trashRunable, undoTime)
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().delete(post)
        }
    }

    fun restoreRemoved(context: Context) {
        handler.removeCallbacks(trashRunable)
        val items = _removed.value!!.toTypedArray()
        trashRunable.run()
        viewModelScope.launch(Dispatchers.IO) {
            RedditDatabase.getInstance(context).favoritesDao().insertAll(*items)
        }
    }

}
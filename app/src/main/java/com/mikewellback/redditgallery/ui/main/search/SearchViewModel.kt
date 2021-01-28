package com.mikewellback.redditgallery.ui.main.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikewellback.redditgallery.api.RedditChildData
import com.mikewellback.redditgallery.api.RedditRetrofit
import com.mikewellback.redditgallery.api.RedditTopPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel: ViewModel() {

    private val _status = MutableLiveData<SearchStatus>().apply {
        value = SearchStatus.DONE
    }
    val status: LiveData<SearchStatus> = _status

    private val _topic = MutableLiveData<String>().apply {
        value = ""
    }
    val topic: LiveData<String> = _topic

    private val _posts = MutableLiveData<List<RedditChildData>>().apply {
        value = listOf()
    }
    val posts: LiveData<List<RedditChildData>> = _posts

    private var _call: Call<RedditTopPosts>? = null
    private var _callId: Long = 0L

    fun fetchTopPosts(topic: String) {
        val callId = System.currentTimeMillis()
        _topic.value = topic
        _callId = callId
        _posts.value = listOf()
        if (topic.isBlank()) {
            _status.value = SearchStatus.DONE
            return
        }
        _status.value = SearchStatus.LOADING
        _call = RedditRetrofit.service.getTopPosts(topic)
        _call?.enqueue(object: Callback<RedditTopPosts> {
            override fun onResponse(call: Call<RedditTopPosts>, response: Response<RedditTopPosts>) {
                if (response.isSuccessful) {
                    val childData = response.body()?.data?.children?.filter {
                        it.data.preview != null && it.data.preview.images.isNotEmpty()
                    }?.map { it.data } ?: listOf()
                    if (callId == _callId) {
                        _status.value = SearchStatus.DONE
                        _posts.value = childData
                    }
                } else {
                    onFailure(call, Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<RedditTopPosts>, t: Throwable) {
                if (callId == _callId) {
                    _status.value = SearchStatus.ERROR
                    _posts.value = listOf()
                }
            }

        })
    }
}
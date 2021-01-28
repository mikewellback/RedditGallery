package com.mikewellback.redditgallery.ui.main.search

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.mikewellback.redditgallery.databinding.FragmentSearchBinding
import com.mikewellback.redditgallery.ui.detail.DetailActivity
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter

class SearchFragment: Fragment() {

    private val searchViewModel: SearchViewModel by activityViewModels()

    private val redditAdapter: RedditAdapter by lazy { RedditAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        context?.also {
            searchViewModel.fetchDatabaseData(it)
        }

        binding.recyclerView.adapter = redditAdapter
        redditAdapter.onItemClickListener = { view, position ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("elements", Gson().toJson(redditAdapter.elements))
            intent.putExtra("position", position)
            val options = ActivityOptions
                .makeSceneTransitionAnimation(
                    activity,
                    view,
                    "image_$position"
                )
            startActivity(intent/*, options.toBundle()*/)
        }
        redditAdapter.onFavoriteClickListener = { imageView, position, element, isFavorite ->
            context?.also {
                if (isFavorite) {
                    searchViewModel.removeFavoriteItem(it, element)
                } else {
                    searchViewModel.addFavoriteItem(it, element)
                }
            }
        }

        searchViewModel.posts.observe(viewLifecycleOwner, {
            redditAdapter.elements = it
            binding.recyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        })

        searchViewModel.favorites.observe(viewLifecycleOwner, {
            redditAdapter.favorites = it.map { it.name }
        })

        searchViewModel.status.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it == SearchStatus.LOADING) View.VISIBLE else View.GONE
        })

        binding.editTextTextPersonName.setText(searchViewModel.topic.value)

        binding.editTextTextPersonName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchViewModel.fetchTopPosts("$p0")
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        return binding.root
    }
}
package com.mikewellback.redditgallery.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikewellback.redditgallery.databinding.FragmentFavoritesBinding
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter

class FavoritesFragment: Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = RedditAdapter()

        favoritesViewModel.posts.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as? RedditAdapter)?.elements = it
            binding.recyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        })

        return binding.root
    }
}
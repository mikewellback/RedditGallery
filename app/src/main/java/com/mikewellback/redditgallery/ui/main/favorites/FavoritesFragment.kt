package com.mikewellback.redditgallery.ui.main.favorites

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.mikewellback.redditgallery.databinding.FragmentFavoritesBinding
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter
import com.mikewellback.redditgallery.ui.detail.DetailActivity

class FavoritesFragment: Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private val redditAdapter: RedditAdapter by lazy { RedditAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        context?.also {
            favoritesViewModel.fetchDatabaseData(it)
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
                    favoritesViewModel.removeFavoriteItem(it, element)
                } else {
                    favoritesViewModel.addFavoriteItem(it, element)
                }
            }
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner, {
            redditAdapter.favorites = it.map { it.name }
            redditAdapter.elements = it
            binding.recyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        })

        return binding.root
    }
}
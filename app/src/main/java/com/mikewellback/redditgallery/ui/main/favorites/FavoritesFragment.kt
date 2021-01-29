package com.mikewellback.redditgallery.ui.main.favorites

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.databinding.FragmentFavoritesBinding
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter
import com.mikewellback.redditgallery.ui.detail.DetailActivity

class FavoritesFragment: Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModels()

    private val redditAdapter: RedditAdapter by lazy { RedditAdapter() }

    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        favoritesViewModel.fetchDatabaseData()

        binding.postsLst.adapter = redditAdapter
        redditAdapter.showSub = true
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
            if (isFavorite) {
                favoritesViewModel.removeFavoriteItem(element, 30_000)
            } else {
                favoritesViewModel.addFavoriteItem(element)
            }
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner, {
            it?.also {
                redditAdapter.favorites = it.map { it.name }
                redditAdapter.elements = it
                binding.postsLst.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
                binding.statusTxt.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
                binding.statusImg.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            }
        })

        favoritesViewModel.removed.observe(viewLifecycleOwner, { items ->
            if (items.isNotEmpty()) {
                context?.also {
                    val text = it.resources.getQuantityString(
                        R.plurals.warning_remove, items.size, items.size
                    )
                    if (snackbar == null) {
                        snackbar = Snackbar.make(binding.root, text, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.action_undo) {
                                favoritesViewModel.restoreRemoved()
                            }
                            .setAnchorView(R.id.navigation_bar)
                    } else {
                        snackbar?.setText(text)
                    }
                    if (snackbar?.isShown == false) {
                        snackbar?.show()
                    }
                }
            } else {
                snackbar?.dismiss()
            }
        })

        return binding.root
    }
}
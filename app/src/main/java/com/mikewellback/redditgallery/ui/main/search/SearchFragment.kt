package com.mikewellback.redditgallery.ui.main.search

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.databinding.FragmentSearchBinding
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter
import com.mikewellback.redditgallery.ui.detail.DetailActivity

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
            binding.statusImg.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            binding.statusTxt.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })

        searchViewModel.favorites.observe(viewLifecycleOwner, {
            redditAdapter.favorites = it.map { it.name }
        })

        searchViewModel.status.observe(viewLifecycleOwner, {
            when (it) {
                SearchStatus.OFFLINE -> {
                    binding.statusTxt.visibility = View.VISIBLE
                    binding.statusImg.visibility = View.VISIBLE
                    binding.statusTxt.setText(R.string.status_offline)
                    binding.statusImg.setImageResource(R.drawable.ic_twotone_signal_cellular_connected_no_internet_0_bar_48)
                }
                SearchStatus.ERROR -> {
                    binding.statusTxt.visibility = View.VISIBLE
                    binding.statusImg.visibility = View.VISIBLE
                    binding.statusTxt.setText(R.string.status_error)
                    binding.statusImg.setImageResource(R.drawable.ic_twotone_stop_screen_share_48)
                }
                SearchStatus.DONE -> {
                    if (binding.searchTxt.text?.isBlank() == true) {
                        binding.statusTxt.visibility = View.VISIBLE
                        binding.statusImg.visibility = View.VISIBLE
                        binding.statusTxt.setText(R.string.status_empty)
                        binding.statusImg.setImageResource(R.drawable.ic_twotone_tag_faces_48)
                    } else {
                        binding.statusTxt.setText(R.string.status_nores)
                        binding.statusImg.setImageResource(R.drawable.ic_twotone_image_search_48)
                        binding.statusTxt.visibility = View.GONE
                        binding.statusImg.visibility = View.GONE
                    }
                }
                else -> {
                    binding.statusTxt.visibility = View.GONE
                    binding.statusImg.visibility = View.GONE
                }
            }
            binding.progressBar.visibility =
                if (it == SearchStatus.LOADING) View.VISIBLE else View.GONE
        })

        binding.searchTxt.setText(searchViewModel.topic.value)

        binding.searchTxt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if ("$p0".trim().length != 1) {
                    searchViewModel.fetchTopPosts("$p0")
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        binding.searchTxt.onFocusChangeListener = View.OnFocusChangeListener { p0, p1 ->
            if (p0 != null && !p1) {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.also {
                    it.hideSoftInputFromWindow(p0.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
                }
            }
        }

        return binding.root
    }
}
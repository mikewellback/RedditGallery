package com.mikewellback.redditgallery.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mikewellback.redditgallery.databinding.FragmentSearchBinding
import com.mikewellback.redditgallery.ui.adapters.RedditAdapter

class SearchFragment: Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = RedditAdapter()

        searchViewModel.posts.observe(viewLifecycleOwner, {
            (binding.recyclerView.adapter as? RedditAdapter)?.elements = it
            binding.recyclerView.visibility = if (it.isEmpty()) View.GONE else View.VISIBLE
        })

        searchViewModel.status.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it == SearchStatus.LOADING) View.VISIBLE else View.GONE
        })

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
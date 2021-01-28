package com.mikewellback.redditgallery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.databinding.ItemPostBinding
import com.mikewellback.redditgallery.models.RedditChildData
import java.util.*

class RedditAdapter: RecyclerView.Adapter<RedditAdapter.RedditVH>() {

    class RedditVH(val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditVH = RedditVH(
        ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: RedditVH, position: Int) {
        val binding = holder.binding
        val item = elements[position]
        val url = item.preview!!.escapedUrl()
        Glide.with(binding.previewImg)
            .load(url)
            .into(binding.previewImg)
        binding.previewImg.transitionName = "image_$position"
        binding.previewImg.setOnClickListener {
            onItemClickListener(it, position)
        }
        val isFavorite = item.name in favorites
        binding.favoriteImg.setImageResource(
            if (isFavorite)
                R.drawable.ic_round_favorite_36
            else
                R.drawable.ic_round_favorite_border_36
        )
        binding.favoriteImg.setOnClickListener {
            onFavoriteClickListener(it as ImageView, position, item, isFavorite)
        }
        binding.subTxt.visibility = if (showSub) View.VISIBLE else View.GONE
        if (showSub) {
            binding.subTxt.text = item.subreddit.toLowerCase(Locale.getDefault())
            when ((item.subreddit.lastOrNull()?.toInt() ?: 0) % 7) {
                0 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_blue) }
                1 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_green) }
                2 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_orange) }
                3 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_purple) }
                4 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_red) }
                5 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_teal) }
                6 -> { binding.subTxt.setBackgroundResource(R.drawable.round_background_yellow) }
            }
        }
        binding.userTxt.text = "/u/${item.author}"
    }

    override fun getItemCount(): Int = elements.size

    var onItemClickListener: (view: View, position: Int) -> Unit = { _, _ -> }
    var onFavoriteClickListener: (imageView: ImageView, position: Int, element: RedditChildData, isFavorite: Boolean) -> Unit = { _, _, _, _ -> }

    var elements = listOf<RedditChildData>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var favorites = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var showSub = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }
}
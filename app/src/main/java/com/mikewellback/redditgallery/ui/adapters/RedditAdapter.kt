package com.mikewellback.redditgallery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.models.RedditChildData
import java.util.*

class RedditAdapter: RecyclerView.Adapter<RedditAdapter.RedditVH>() {

    class RedditVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var favorite_img: ImageView = itemView.findViewById(R.id.favorite_img)
        var user_txt: TextView = itemView.findViewById(R.id.user_txt)
        var sub_txt: TextView = itemView.findViewById(R.id.sub_txt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return RedditVH(view)
    }

    override fun onBindViewHolder(holder: RedditVH, position: Int) {
        val elem = elements[position]
        val url = elem.preview!!.escapedUrl()
        Glide.with(holder.imageView)
            .load(url)
            .into(holder.imageView)
        holder.imageView.transitionName = "image_$position"
        holder.imageView.setOnClickListener {
            onItemClickListener(it, position)
        }
        val isFavorite = elem.name in favorites
        holder.favorite_img.setImageResource(
            if (isFavorite)
                R.drawable.ic_round_favorite_36
            else
                R.drawable.ic_round_favorite_border_36
        )
        holder.favorite_img.setOnClickListener {
            onFavoriteClickListener(it as ImageView, position, elem, isFavorite)
        }
        holder.sub_txt.visibility = if (showSub) View.VISIBLE else View.GONE
        if (showSub) {
            holder.sub_txt.text = elem.subreddit.toLowerCase(Locale.getDefault())
            when ((elem.subreddit.lastOrNull()?.toInt() ?: 0) % 7) {
                0 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_blue) }
                1 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_green) }
                2 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_orange) }
                3 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_purple) }
                4 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_red) }
                5 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_teal) }
                6 -> { holder.sub_txt.setBackgroundResource(R.drawable.round_background_yellow) }
            }
        }
        holder.user_txt.text = "/u/${elem.author}"
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
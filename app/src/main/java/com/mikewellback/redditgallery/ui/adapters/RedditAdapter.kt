package com.mikewellback.redditgallery.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikewellback.redditgallery.R
import com.mikewellback.redditgallery.api.RedditChildData

class RedditAdapter: RecyclerView.Adapter<RedditAdapter.RedditVH>() {

    class RedditVH(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedditVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return RedditVH(view)
    }

    override fun onBindViewHolder(holder: RedditVH, position: Int) {
        val url = HtmlCompat.fromHtml(
            elements[position].preview!!.images.first().source.url,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).toString()
        Glide.with(holder.imageView)
            .load(url)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = elements.size

    var elements = listOf<RedditChildData>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
}
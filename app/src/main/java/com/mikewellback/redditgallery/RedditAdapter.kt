package com.mikewellback.redditgallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikewellback.redditgallery.api.RedditChildData
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

class RedditAdapter: RecyclerView.Adapter<RedditAdapter.RedditVH>() {
    class RedditVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RedditVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)

        return RedditVH(view)
    }

    override fun onBindViewHolder(holder: RedditVH, position: Int) {
        Glide.with(holder.imageView)
            .load(elements[position].preview!!.images.first().source.url
                .replace("&amp;", "&", true)
            )
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = elements.size

    var elements = listOf<RedditChildData>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }
}
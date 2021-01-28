package com.mikewellback.redditgallery.ui.adapters

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.text.HtmlCompat
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mikewellback.redditgallery.api.RedditChildData
import uk.co.senab.photoview.PhotoView

class GalleryPagerAdapter(
    private val elements: List<RedditChildData>,
    private val onZoomChange: (zoomed: Boolean) -> Unit
): PagerAdapter() {

    override fun getCount(): Int = elements.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(container.context)
        photoView.setOnMatrixChangeListener {
            onZoomChange(it.width() > container.width)
        }
        photoView.transitionName = "image"

        val url = HtmlCompat.fromHtml(
            elements[position].preview!!.images.first().source.url,
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).toString()
        Glide.with(photoView.context)
            .load(url)
            .into(photoView)

        container.addView(
            photoView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        return photoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`
}
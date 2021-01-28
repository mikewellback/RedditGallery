package com.mikewellback.redditgallery.ui.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mikewellback.redditgallery.models.RedditChildData
import com.mikewellback.redditgallery.databinding.ActivityDetailBinding
import com.mikewellback.redditgallery.ui.adapters.GalleryPagerAdapter

class DetailActivity: AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    var galleryPagerAdapter: GalleryPagerAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.closeImg.setOnClickListener {
            finish()
        }

        if (intent.extras != null &&
            intent.hasExtra("position") && intent.hasExtra("elements")) {
            val position = intent.extras!!.get("position") as Int
            val elements = Gson().fromJson<List<RedditChildData>>(
                intent.extras!!.get("elements") as String,
                object: TypeToken<List<RedditChildData?>?>() {}.type)

            galleryPagerAdapter = GalleryPagerAdapter(elements) { zoomed ->
                binding.containerPag.swipeable = !zoomed
                if (zoomed) {
                    binding.backgroundLay.visibility = View.GONE
                    binding.userTxt.visibility = View.GONE
                    binding.descriptionTxt.visibility = View.GONE
                } else {
                    binding.backgroundLay.visibility = View.VISIBLE
                    binding.userTxt.visibility = View.VISIBLE
                    binding.descriptionTxt.visibility = View.VISIBLE
                }
            }
            binding.containerPag.adapter = galleryPagerAdapter
            val onPageChangeListener = object: ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

                override fun onPageSelected(position: Int) {
                    binding.backgroundLay.animate().alpha(0.6f)
                    binding.userTxt.animate().alpha(1f)
                    binding.descriptionTxt.animate().alpha(1f)
                    binding.userTxt.text = "/u/${elements[position].author}"
                    binding.descriptionTxt.text = elements[position].title
                }

                override fun onPageScrollStateChanged(state: Int) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                        binding.backgroundLay.animate().alpha(0f)
                        binding.userTxt.animate().alpha(0f)
                        binding.descriptionTxt.animate().alpha(0f)
                    } else {
                        binding.backgroundLay.animate().alpha(0.6f)
                        binding.userTxt.animate().alpha(1f)
                        binding.descriptionTxt.animate().alpha(1f)
                    }
                }
            }
            binding.containerPag.addOnPageChangeListener(onPageChangeListener)
            binding.containerPag.currentItem = position
            onPageChangeListener.onPageSelected(position)
        }
    }

}
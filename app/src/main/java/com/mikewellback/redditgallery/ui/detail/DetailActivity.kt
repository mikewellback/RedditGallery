package com.mikewellback.redditgallery.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mikewellback.redditgallery.api.RedditChildData
import com.mikewellback.redditgallery.databinding.ActivityDetailBinding
import com.mikewellback.redditgallery.ui.adapters.GalleryPagerAdapter

class DetailActivity: AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        if (intent.extras != null &&
            intent.hasExtra("position") && intent.hasExtra("elements")) {
            val position = intent.extras!!.get("position") as Int
            val elements = Gson().fromJson<List<RedditChildData>>(
                intent.extras!!.get("elements") as String,
                object: TypeToken<List<RedditChildData?>?>() {}.type)

            binding.viewPager.adapter = GalleryPagerAdapter(elements) { zoomed ->
                binding.viewPager.swipeable = !zoomed
            }
            binding.viewPager.currentItem = position
        }
    }

}
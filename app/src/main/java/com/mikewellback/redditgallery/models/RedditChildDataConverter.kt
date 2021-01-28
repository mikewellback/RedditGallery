package com.mikewellback.redditgallery.models

import androidx.room.TypeConverter

class RedditChildDataConverter {

    @TypeConverter
    fun fromPreview(preview: RedditImages?): String = preview?.sourceUrl() ?: ""

    @TypeConverter
    fun toPreview(url: String): RedditImages = RedditImages(
        arrayOf(RedditImage(RedditImageData(url, 0, 0),
            arrayOf<RedditImageData>())))

}

package com.mikewellback.redditgallery.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mikewellback.redditgallery.models.RedditChildData
import com.mikewellback.redditgallery.models.RedditChildDataConverter

@Database(entities = [RedditChildData::class], version = 1)
@TypeConverters(RedditChildDataConverter::class)
abstract class RedditDatabase: RoomDatabase() {
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        @Volatile private var instance: RedditDatabase? = null

        fun initDB(context: Context) {
            synchronized(this) {
                buildDatabase(context).also { instance = it }
            }
        }

        fun getInstance(): RedditDatabase {
            if (instance == null) {
                throw InstantiationException("DB hasn't been initialized")
            }
            return instance!!
        }

        private fun buildDatabase(context: Context): RedditDatabase {
            return Room.databaseBuilder(context,
                RedditDatabase::class.java, "reddit-gallery")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
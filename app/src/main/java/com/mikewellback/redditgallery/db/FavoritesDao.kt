package com.mikewellback.redditgallery.db

import androidx.room.*
import com.mikewellback.redditgallery.models.RedditChildData
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorites ORDER BY created")
    fun getAll(): Flow<List<RedditChildData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg favorites: RedditChildData)

    @Delete
    fun delete(favorite: RedditChildData)
}
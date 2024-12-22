package com.example.newsappcompose.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews

@Dao
interface NewsDao {

    @Query("SELECT*FROM news_table")
    suspend fun getFavoriteNews():List<FavoriteNews>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertNews(favoriteNews:FavoriteNews)

    @Delete
    suspend fun deleteNews(favoriteNews: FavoriteNews)
}
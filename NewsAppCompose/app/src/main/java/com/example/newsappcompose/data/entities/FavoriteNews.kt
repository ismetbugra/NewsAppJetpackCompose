package com.example.newsappcompose.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_table")
data class FavoriteNews(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    //val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    val isFavorite:Int=0
) {
}
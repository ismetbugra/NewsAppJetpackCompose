package com.example.newsappcompose.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.newsappcompose.data.room.Converters


data class Article(
    val id:Int?=null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String,
    val isFavorite:Int=0
)
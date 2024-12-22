package com.example.newsappcompose.data.entities

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)
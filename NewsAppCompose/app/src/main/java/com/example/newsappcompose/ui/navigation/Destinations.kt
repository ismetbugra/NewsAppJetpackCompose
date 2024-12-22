package com.example.newsappcompose.ui.navigation

import com.example.newsappcompose.data.entities.Source
import kotlinx.serialization.Serializable

@Serializable
sealed class Destinations {

    @Serializable
    object Home:Destinations()

    @Serializable
    object Favorites:Destinations()

    @Serializable
    object Splash:Destinations()

    @Serializable
    object OnBoarding:Destinations()

    @Serializable
    data class Detail(
        val author: String?,
        val content: String?,
        val description: String?,
        val publishedAt: String?,
        val title: String?,
        val url: String?,
        val urlToImage: String?,
        val isFavorite:Int,
        val favId:Int
    )

    @Serializable
    object Search :Destinations()

    @Serializable
    data class WebViewScreen(
        val newsUrl:String?
    ) :Destinations()


}
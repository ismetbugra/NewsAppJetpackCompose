package com.example.newsappcompose.data.repository

import com.example.newsappcompose.data.datasource.NewsDataSource
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews
import com.example.newsappcompose.data.entities.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(var nds:NewsDataSource) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int): Response<NewsResponse> = nds.getBreakingNews(countryCode,pageNumber)
    suspend fun getSearchNews(searchQuery:String,pageNumber:Int):Response<NewsResponse> = nds.getSearchNews(searchQuery,pageNumber)
    suspend fun getFavoriteNews():List<FavoriteNews> = nds.getFavoriteNews()
    suspend fun upsertNews(favoriteNews: FavoriteNews) = nds.upsertNews(favoriteNews)
    suspend fun deleteNews(favoriteNews: FavoriteNews) = nds.deleteNews(favoriteNews)

}
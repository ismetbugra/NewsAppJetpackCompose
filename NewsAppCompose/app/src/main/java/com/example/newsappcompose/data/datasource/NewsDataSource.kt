package com.example.newsappcompose.data.datasource

import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews
import com.example.newsappcompose.data.entities.NewsResponse
import com.example.newsappcompose.data.retrofit.NewsApi
import com.example.newsappcompose.data.room.NewsDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsDataSource @Inject constructor(var ndao:NewsApi,var newsDao: NewsDao) {
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int):Response<NewsResponse> =
        withContext(Dispatchers.IO){
            return@withContext ndao.getBreakingNews(countryCode,pageNumber)
        }

    suspend fun getSearchNews(searchQuery:String,pageNumber:Int):Response<NewsResponse> =
        withContext(Dispatchers.IO){
            return@withContext ndao.searchForNews(searchQuery,pageNumber)
        }

    suspend fun getFavoriteNews():List<FavoriteNews> =
        withContext(Dispatchers.IO){
            return@withContext newsDao.getFavoriteNews()
        }

    suspend fun upsertNews(favoriteNews: FavoriteNews){

        newsDao.upsertNews(favoriteNews)
    }

    suspend fun deleteNews(favoriteNews: FavoriteNews){
        newsDao.deleteNews(favoriteNews)
    }
}

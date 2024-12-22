package com.example.newsappcompose.data.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews
import com.example.newsappcompose.data.entities.NewsResponse
import com.example.newsappcompose.data.repository.NewsRepository
import com.example.newsappcompose.utils.Constants
import com.example.newsappcompose.utils.Resource
import com.example.newsappcompose.utils.network.rememberConnectivityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(var nrepo:NewsRepository):AndroidViewModel(application = Application()) {

    var breakingNewsList = mutableStateOf<List<Article>>(listOf())
    var breakingNewsPageNumber = 1

    var searchNewsList = mutableStateOf<List<Article>>(listOf())
    var searchNewsPageNumber=1

    var favoriteNewsList = mutableStateOf<List<FavoriteNews>>(listOf())

    var countryCode= Constants.country

    var loadingState= mutableStateOf(false)
    var errorState = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    var favoriteLoadingState= mutableStateOf(false)

    init {
        //getBreakingNews()   detail screene geçince viewmodel olusunca apiden veri çekiyor internet yoksa patlıyor gerek yok buna
        getFavoriteNewsList()
    }

    fun getBreakingNews(){
        CoroutineScope(Dispatchers.IO).launch {
            loadingState.value=true
            val response= nrepo.getBreakingNews(countryCode,breakingNewsPageNumber)
            val resource =handleBreakingNewsResponse(response)

            // dönen verileri handle etme işlemi
            when(resource){
                is Resource.Success -> {
                    resource.data?.let {
                        errorState.value=false
                        loadingState.value=false
                        breakingNewsList.value=it.articles
                    }
                }
                is Resource.Error -> {
                    loadingState.value=false
                    errorState.value=true
                    errorMessage.value=resource.message!!
                }

                else -> {}
            }
        }

    }

    fun getSearchNews(searchQuery:String){
        CoroutineScope(Dispatchers.IO).launch{
            loadingState.value=true
            val response= nrepo.getSearchNews(searchQuery,searchNewsPageNumber)
            val resource = handleSearchNewsResponse(response)

            when(resource){
                is Resource.Success -> {
                    resource.data?.let {
                        errorState.value=false
                        loadingState.value=false
                        searchNewsList.value=it.articles
                    }
                }
                is Resource.Error -> {
                    loadingState.value=false
                    errorState.value=true
                    errorMessage.value=resource.message!!
                }
                else ->{}
            }
        }
    }

    fun handleBreakingNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun handleSearchNewsResponse(response: Response<NewsResponse>):Resource<NewsResponse>{

        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun getFavoriteNewsList(){
        CoroutineScope(Dispatchers.IO).launch {
            favoriteLoadingState.value=true
            favoriteNewsList.value= nrepo.getFavoriteNews()
            favoriteLoadingState.value=false
        }
    }

    fun upsertFavoriteNews(favoriteNews: FavoriteNews){
        CoroutineScope(Dispatchers.IO).launch {
            nrepo.upsertNews(favoriteNews)
            getFavoriteNewsList()
        }
    }

    fun deleteFavoriteNews(favoriteNews: FavoriteNews){
        CoroutineScope(Dispatchers.IO).launch {
            nrepo.deleteNews(favoriteNews)
            getFavoriteNewsList()
        }
    }

}
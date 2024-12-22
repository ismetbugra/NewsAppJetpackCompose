package com.example.newsappcompose.data.retrofit

import com.example.newsappcompose.utils.Constants.BASE_URL

class ApiUtils {
    companion object{
        fun getNewsApi():NewsApi{
            return RetrofitClient
                .getClient(BASE_URL)
                .create(NewsApi::class.java)
        }
    }
}
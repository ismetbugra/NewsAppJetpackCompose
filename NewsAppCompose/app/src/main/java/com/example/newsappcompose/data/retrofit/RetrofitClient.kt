package com.example.newsappcompose.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object{
        fun getClient(BASE_URL:String):Retrofit{
            return Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
        }
    }
}
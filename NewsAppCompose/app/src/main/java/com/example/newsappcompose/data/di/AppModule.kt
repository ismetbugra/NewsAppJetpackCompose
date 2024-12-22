package com.example.newsappcompose.data.di

import android.content.Context
import androidx.room.Room
import com.example.newsappcompose.data.datasource.NewsDataSource
import com.example.newsappcompose.data.repository.NewsRepository
import com.example.newsappcompose.data.retrofit.ApiUtils
import com.example.newsappcompose.data.retrofit.NewsApi
import com.example.newsappcompose.data.room.NewsDao
import com.example.newsappcompose.data.room.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideNewsRepository(nds:NewsDataSource):NewsRepository{
        return NewsRepository(nds)
    }

    @Provides
    @Singleton
    fun provideNewsDataSource(ndao:NewsApi,newsDao: NewsDao):NewsDataSource{
        return NewsDataSource(ndao,newsDao)
    }

    @Provides
    @Singleton
    fun provideNewsApi():NewsApi{
        return ApiUtils.getNewsApi()
    }

    @Provides
    @Singleton
    fun provideNewsDao(@ApplicationContext context: Context):NewsDao{
        return Room.databaseBuilder(context.applicationContext,NewsDatabase::class.java,
            "news_database.sqlite").build().getNewsDao()
    }
}
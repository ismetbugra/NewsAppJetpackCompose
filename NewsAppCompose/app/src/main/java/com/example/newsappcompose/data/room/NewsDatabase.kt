package com.example.newsappcompose.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsappcompose.data.entities.Article
import com.example.newsappcompose.data.entities.FavoriteNews

@Database(entities = [FavoriteNews::class], version = 1)
@TypeConverters(Converters::class)
abstract class NewsDatabase():RoomDatabase() {

    abstract fun getNewsDao():NewsDao
}
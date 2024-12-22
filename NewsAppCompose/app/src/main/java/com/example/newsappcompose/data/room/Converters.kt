package com.example.newsappcompose.data.room

import androidx.room.TypeConverter
import com.example.newsappcompose.data.entities.Source

class Converters {

    //Bu metod, Source türündeki bir nesneyi alır ve bu nesnenin name özelliğini bir String olarak döndürür.
    // Bu, Source nesnesini veritabanına kaydederken onu bir String'e dönüştürmek için kullanılır.
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }


    //Bu metod, bir String'i alır ve bu String'i kullanarak yeni bir Source nesnesi oluşturur.
    // Bu, veritabanından bir String okunduğunda onu bir Source nesnesine dönüştürmek için kullanılır.
    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}
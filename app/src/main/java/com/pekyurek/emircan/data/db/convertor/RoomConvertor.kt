package com.pekyurek.emircan.data.db.convertor

import androidx.room.TypeConverter
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

abstract class BaseConvertor<T>(val type: Type) {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val dataAdapter = moshi.adapter<T>(type)

    @TypeConverter
    fun stringToData(string: String): T? {
        return dataAdapter.fromJson(string)
    }


    @TypeConverter
    fun dataToString(data: T): String {
        return dataAdapter.toJson(data)
    }
}

class UserConvertor : BaseConvertor<User>(User::class.java)

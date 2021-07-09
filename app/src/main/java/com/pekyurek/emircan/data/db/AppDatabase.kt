package com.pekyurek.emircan.data.db

import androidx.annotation.Keep
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pekyurek.emircan.data.db.convertor.UserConvertor
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User

@Database(entities = [User::class, Message::class], version = 1, exportSchema = false)
@TypeConverters(UserConvertor::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun userDao(): UserDao

    @Keep
    companion object {
        val DATABASE_NAME = AppDatabase::class.java.simpleName
    }

}
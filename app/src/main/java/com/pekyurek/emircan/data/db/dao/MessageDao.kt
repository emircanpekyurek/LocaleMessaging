package com.pekyurek.emircan.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pekyurek.emircan.data.db.dao.base.BaseDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message

@Dao
interface MessageDao : BaseDao<Message> {

    @Transaction
    @Query("DELETE FROM message")
    fun deleteAllMessages()

    @Query("SELECT * FROM message")
    fun getAllMessages(): List<Message>
}
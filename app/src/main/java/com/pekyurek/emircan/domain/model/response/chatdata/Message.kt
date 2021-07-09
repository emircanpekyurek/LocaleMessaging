package com.pekyurek.emircan.domain.model.response.chatdata

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pekyurek.emircan.domain.model.response.user.User
import com.squareup.moshi.Json

@Entity(tableName = "message")
data class Message(
    @PrimaryKey
    @Json(name = "id")
    val id: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "timestamp")
    val timestamp: Long,
    @Json(name = "user")
    val user: User

)
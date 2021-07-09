package com.pekyurek.emircan.domain.model.response.user

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @Json(name = "id")
    val id: String,
    @Json(name = "avatarURL")
    val avatarURL: String,
    @Json(name = "nickname")
    val nickname: String
) : Parcelable
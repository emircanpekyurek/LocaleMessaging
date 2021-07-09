package com.pekyurek.emircan.data.repository

import com.pekyurek.emircan.domain.model.response.chatdata.ChatData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/files/chatdata-example.json")
    suspend fun chatData(): Response<ChatData>

}
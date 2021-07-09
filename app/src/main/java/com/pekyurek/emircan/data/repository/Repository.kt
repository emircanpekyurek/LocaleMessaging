package com.pekyurek.emircan.data.repository

import com.pekyurek.emircan.domain.model.response.chatdata.ChatData
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun chatData(): Flow<ResultStatus<ChatData>>

}
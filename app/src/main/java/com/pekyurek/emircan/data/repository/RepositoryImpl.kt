package com.pekyurek.emircan.data.repository

import com.pekyurek.emircan.domain.model.response.chatdata.ChatData
import com.pekyurek.emircan.data.repository.locale.LocaleDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val localeDataSource: LocaleDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repository {

    override suspend fun chatData(): Flow<ResultStatus<ChatData>> {
        val localeData = localeDataSource.getMessages()
        return if (localeData.isNullOrEmpty()) {
            flow {
                remoteDataSource.chatData().collect { result ->
                    if (result is ResultStatus.Success) {
                        localeDataSource.setMessages(result.data.messages)
                        localeDataSource.insertUsers(result.data.messages.map { it.user })
                    }
                    emit(result)
                }
            }
        } else {
            flow { emit(ResultStatus.Success(ChatData(localeData))) }
        }
    }

}
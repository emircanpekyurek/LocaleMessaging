package com.pekyurek.emircan.domain.usecase.chatdata

import com.pekyurek.emircan.data.repository.Repository
import com.pekyurek.emircan.data.repository.ResultStatus
import com.pekyurek.emircan.domain.model.response.chatdata.ChatData
import com.pekyurek.emircan.domain.usecase.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatDataUseCase @Inject constructor(
    private val repository: Repository
) : UseCase<Any, ChatData> {

    override suspend fun execute(request: Any?): Flow<ResultStatus<ChatData>> {
        return repository.chatData()
    }

}
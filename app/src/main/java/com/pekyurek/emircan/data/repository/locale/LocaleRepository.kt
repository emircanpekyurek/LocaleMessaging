package com.pekyurek.emircan.data.repository.locale

import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User

interface LocaleRepository {

    suspend fun insertUsers(users: List<User>)

    suspend fun setMessages(messages: List<Message>)

    suspend fun getMessages(): List<Message>

}


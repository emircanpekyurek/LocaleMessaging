package com.pekyurek.emircan.data.repository.locale

import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import javax.inject.Inject

class LocaleDataSource @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao
) : LocaleRepository {

    override suspend fun insertUsers(users: List<User>) {
        userDao.insert(users)
    }

    override suspend fun setMessages(messages: List<Message>) {
        messageDao.deleteAllMessages()
        messageDao.insert(messages)
    }

    override suspend fun getMessages(): List<Message> {
        return messageDao.getAllMessages()
    }

}
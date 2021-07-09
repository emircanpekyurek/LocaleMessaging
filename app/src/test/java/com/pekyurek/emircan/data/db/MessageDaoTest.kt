package com.pekyurek.emircan.data.db

import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
internal class MessageDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var messageDao: MessageDao

    private val userList = listOf(
        User("1", "", "test1"),
        User("2", "", "test2"),
        User("3", "", "test3")
    )

    private val messageList = listOf(
        Message("1", "message-1", 2131231, userList[0]),
        Message("2", "message-2", 213123131, userList[1]),
        Message("3", "message-3", 83131231, userList[2])

    )


    @Before
    fun setUp() {
        hiltRule.inject()
        messageDao.insert(messageList)
    }

    @Test
    fun `insert single message`() {
        //Given
        val message = Message("4", "message-4", 3123124214, userList[2])
        assert(messageDao.getAllMessages().contains(message).not())

        //When
        messageDao.insert(message)

        //Then
        assert(messageDao.getAllMessages().contains(message))
    }


    @Test
    fun `insert multiple messages`() {
        //Given
        val message1 = Message("4", "message-4", 6343124214, userList[0])
        val message2 = Message("5", "message-5", 367124214, userList[1])
        val message3 = Message("6", "message-6", 94213124214, userList[2])
        assert(messageDao.getAllMessages().contains(message1).not())
        assert(messageDao.getAllMessages().contains(message2).not())
        assert(messageDao.getAllMessages().contains(message3).not())

        //When
        messageDao.insert(listOf(message1, message2, message3))

        //Then
        assert(messageDao.getAllMessages().contains(message1))
        assert(messageDao.getAllMessages().contains(message2))
        assert(messageDao.getAllMessages().contains(message3))
    }

    @Test
    fun `delete all messages`() {
        //Given
        assert(messageDao.getAllMessages().isNotEmpty())

        //When
        messageDao.deleteAllMessages()

        //Then
        assert(messageDao.getAllMessages().isEmpty())
    }

    @Test
    fun `get all messages`() {
        assert(messageDao.getAllMessages() == messageList)
    }


    @After
    fun closeDb() {
        appDatabase.close()
    }

}
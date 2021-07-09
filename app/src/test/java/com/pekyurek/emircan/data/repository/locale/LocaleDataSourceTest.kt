package com.pekyurek.emircan.data.repository.locale

import com.pekyurek.emircan.data.db.AppDatabase
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import javax.inject.Inject


@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
internal class LocaleDataSourceTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)


    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var localeDataSource: LocaleDataSource

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `insertUsers() for empty user`() {
        runBlocking {
            //Given
            val user1 = User("1", "", "nick1")
            val user2 = User("2", "", "nick2")
            userDao.insert(listOf(user1, user2))
            assert(userDao.getAllUsers().isNotEmpty())
            val dbUsers = userDao.getAllUsers()

            //When
            localeDataSource.insertUsers(emptyList())

            //Then
            assert(userDao.getAllUsers() == dbUsers)
        }
    }


    @Test
    fun `insertUsers() for not empty user`() {
        runBlocking {
            //Given
            val oldUser = User("11", "", "nick11")
            userDao.insert(oldUser)
            assert(userDao.getAllUsers().isNotEmpty())

            val user1 = User("1", "", "nick1")
            val user2 = User("2", "", "nick2")
            val user3 = User("1", "", "nick3")
            val user4 = User("4", "", "nick4")
            val user5 = User("3", "", "nick4")
            val user6 = User("4", "", "nick4")
            val newUserList = listOf(user1, user2, user3, user4, user5, user6)

            //When
            localeDataSource.insertUsers(newUserList)

            //Then
            val dbUsers = userDao.getAllUsers()
            assert(dbUsers.size == 5)
            assert(dbUsers[0] == oldUser)
            assert(dbUsers[1] == user2)
            assert(dbUsers[2] == user3)
            assert(dbUsers[3] == user5)
            assert(dbUsers[4] == user6)
        }
    }

    @Test
    fun `setMessages() for empty message`() {
        runBlocking {
            //Given
            val user1 = User("1", "", "nick1")
            val user2 = User("2", "", "nick2")
            val message1 = Message("1", "", System.currentTimeMillis(), user1)
            val message2 = Message("2", "", System.currentTimeMillis(), user2)
            messageDao.insert(listOf(message1, message2))
            assert(messageDao.getAllMessages().isNotEmpty())

            //When
            localeDataSource.setMessages(emptyList())

            //Then
            assert(messageDao.getAllMessages().isEmpty())
        }
    }


    @Test
    fun `setMessages() for not empty message`() {
        runBlocking {
            //Given

            val user1 = User("1", "", "nick1")
            val user2 = User("2", "", "nick2")
            val oldMessage = Message("231", "", System.currentTimeMillis(), user1)
            val message1 = Message("1", "", System.currentTimeMillis(), user1)
            val message2 = Message("2", "", System.currentTimeMillis(), user2)
            val messageList = listOf(message1, message2)
            messageDao.insert(oldMessage)
            assert(messageDao.getAllMessages().isNotEmpty())

            //When
            localeDataSource.setMessages(messageList)

            //Then
            assert(messageDao.getAllMessages() == messageList)
        }
    }

    @Test
    fun getMessages() {
        runBlocking {
            //Given
            val user1 = User("1", "", "nick1")
            val user2 = User("2", "", "nick2")
            val message1 = Message("1", "", System.currentTimeMillis(), user1)
            val message2 = Message("2", "", System.currentTimeMillis(), user2)
            val message3 = Message("3", "", System.currentTimeMillis(), user1)
            val message4 = Message("4", "", System.currentTimeMillis(), user2)
            val message5 = Message("5", "", System.currentTimeMillis(), user2)
            val messageList = listOf(message1, message2, message3, message4, message5)
            messageDao.insert(messageList)

            //When
            val localMessages = localeDataSource.getMessages()

            //Then
            assert(localMessages == messageList)
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }
}
package com.pekyurek.emircan.presentation.ui.message

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pekyurek.emircan.data.db.AppDatabase
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.pref.AppPreference
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import com.pekyurek.emircan.domain.usecase.chatdata.ChatDataUseCase
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
import javax.inject.Inject

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
internal class MessageViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var messageViewModel: MessageViewModel

    @Inject
    lateinit var chatDataUseCase: ChatDataUseCase

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var appPreference: AppPreference

    @Inject
    lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        hiltRule.inject()
        messageViewModel = MessageViewModel(chatDataUseCase, messageDao, appPreference)
    }

    @Test
    fun `loadMessageList() from local data source`() {
        runBlocking {
            //Given
            val loggedInUser = User("111", "url", "dummy111")

            val user1 = User("1", "url", "dummy1")
            val user2 = User("2", "url", "dummy2")

            val message1 = Message("1", "text-1", System.currentTimeMillis(), user1)
            val message2 = Message("2", "text-2", System.currentTimeMillis(), user2)
            val message3 = Message("3", "text-3", System.currentTimeMillis(), user2)
            val message4 = Message("4", "text-3", System.currentTimeMillis(), user1)
            val message5 = Message("5", "text-4", System.currentTimeMillis(), user2)

            val messageList = listOf(message1, message2, message3, message4, message5)

            // userDao.insert(listOf(user1, user2))
            messageDao.insert(messageList)

            //When
            messageViewModel.loadMessageList(loggedInUser).join()

            //Then
            assert(messageViewModel.loading.value == null)
            assert(messageViewModel.messageList.value == messageList)
            assert(messageViewModel.loggedInUser.value == loggedInUser)
        }
    }

    @Test
    fun `loadMessageList() from remote data source`() {
        runBlocking {
            //Given
            val loggedInUser = User("111", "url", "dummy111")
            messageDao.deleteAllMessages()

            //When
            messageViewModel.loadMessageList(loggedInUser).join()

            //Then
            assert(messageViewModel.loading.value == false)
            assert(messageViewModel.messageList.value!!.isNotEmpty())
            assert(messageViewModel.loggedInUser.value == loggedInUser)
        }
    }

    @Test
    fun postNewMessage() {
        //Given
        val loggedInUser = User("111", "url", "dummy111")
        messageViewModel.loggedInUser.value = loggedInUser
        val messageText = "testMessage"

        //When
        messageViewModel.postNewMessage(messageText)

        //Then
        assert(messageViewModel.postNewMessage.value!!.text == messageText)
        assert(messageViewModel.postNewMessage.value!!.user == loggedInUser)
    }

    @Test
    fun `createNewMessage() not blank Text`() {
        //Given
        val loggedInUser = User("111", "url", "dummy111")
        messageViewModel.loggedInUser.value = loggedInUser
        val messageText = "test message"
        val currentTime = System.currentTimeMillis()

        //When
        val message = messageViewModel.createNewMessage(messageText)

        //Then
        assert(message != null)
        assert(message!!.timestamp == currentTime)
        assert(message.id == currentTime.toString())
        assert(message.text == messageText)
        assert(messageDao.getAllMessages().map { it.id }.contains(message.id).not())
    }

    @Test
    fun `createNewMessage() blank Text`() {
        //Given
        val blankText = "    "
        val emptyText = ""

        //When
        val message1 = messageViewModel.createNewMessage(blankText)
        val message2 = messageViewModel.createNewMessage(emptyText)

        //Then
        assert(message1 == null)
        assert(message2 == null)
    }


    @Test
    fun logout() {
        //Given
        val dummyLoggedInUserId = "321"
        val loggedInUser = User(dummyLoggedInUserId, "url", "nick")
        messageViewModel.loggedInUser.postValue(loggedInUser)
        appPreference.saveLoggedInUserId(messageViewModel.loggedInUser.value!!.id)

        assert(appPreference.getLoggedInUserId() == dummyLoggedInUserId)
        assert(messageViewModel.loggedInUser.value == loggedInUser)
        assert(messageViewModel.logout.value == null)

        //When
        messageViewModel.logout()

        //Then
        assert(appPreference.getLoggedInUserId() == null)
        assert(messageViewModel.logout.value == loggedInUser)
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }


}
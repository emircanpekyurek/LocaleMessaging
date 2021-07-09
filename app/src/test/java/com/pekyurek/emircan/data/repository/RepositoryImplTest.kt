package com.pekyurek.emircan.data.repository

import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
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
internal class RepositoryImplTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repositoryImpl: RepositoryImpl

    @Inject
    lateinit var messageDao: MessageDao

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `chatData() from remote repository`() {
        runBlocking {
            //Given
            messageDao.deleteAllMessages()
            assert(messageDao.getAllMessages().isNullOrEmpty())

            //When
            val result = repositoryImpl.chatData().toList()

            //Then
            assert(result.size == 3)
            assert(result[0] is ResultStatus.Loading)
            assert((result[0] as ResultStatus.Loading).show)
            assert((result[1] is ResultStatus.Success))
            assert((result[1] as ResultStatus.Success).data.messages.isNotEmpty())
            assert(result[2] is ResultStatus.Loading)
            assert((result[2] as ResultStatus.Loading).show.not())
        }
    }

    @Test
    fun `chatData() from local repository`() {
        runBlocking {
            //Given
            val message = Message("id1", "message-1", 3123124214, User("1", "", "nick"))
            messageDao.insert(message)
            assert(messageDao.getAllMessages().isNullOrEmpty().not())

            //When
            val result = repositoryImpl.chatData().toList()

            //Then
            assert(result.size == 1)
            assert(result[0] is ResultStatus.Success)
            assert((result[0] as ResultStatus.Success).data.messages == listOf(message))
        }

    }
}

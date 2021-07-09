package com.pekyurek.emircan.data.db

import com.pekyurek.emircan.data.db.dao.UserDao
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
internal class UserDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var userDao: UserDao

    private val userList = listOf(
        User("1", "", "test1"),
        User("2", "", "test2"),
        User("3", "", "test3")
    )

    @Before
    fun setUp() {
        hiltRule.inject()
        userDao.insert(userList)
    }

    @Test
    fun `delete all users`() {
        //Given
        assert(userDao.getAllUsers().isNotEmpty())

        //When
        userDao.deleteAllUser()

        //Then
        assert(userDao.getAllUsers().isEmpty())

    }

    @Test
    fun `get user from database with user id`() {
        //Given
        val user = userList[0]

        //When
        val databaseUser = userDao.getUserById(user.id)

        //Then
        assert(user == databaseUser)
    }

    @Test
    fun `get user from database with user nickname`() {
        //Given
        val user = userList[0]

        //When
        val databaseUser = userDao.getUserByNickname(user.nickname)

        //Then
        assert(user == databaseUser)
    }

    @Test
    fun `get all users from database`() {
        assert(userList == userDao.getAllUsers())
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }
}
package com.pekyurek.emircan.presentation.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.pekyurek.emircan.R
import com.pekyurek.emircan.data.db.AppDatabase
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.data.pref.AppPreference
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
internal class MainViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var userDao: UserDao

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var appPreference: AppPreference

    @Before
    fun setUp() {
        hiltRule.inject()
        mainViewModel = MainViewModel(userDao, messageDao, appPreference)
    }

    @Test
    fun `checkForAutoLogin() autoLogin success case`() {
        runBlocking {
            //Given
            val loggedInUser = User("1", "url", "dummyNick")
            appPreference.saveLoggedInUserId(loggedInUser.id)
            userDao.insert(loggedInUser)

            //When
            mainViewModel.checkForAutoLogin().join()

            //Then
            assert(appPreference.getLoggedInUserId() == loggedInUser.id)
            assert(userDao.getAllUsers().contains(loggedInUser))
            assert(mainViewModel.loggedInUser.value == loggedInUser)
        }
    }

    @Test
    fun `checkForAutoLogin() autoLogin fail case when userId is empty in pref`() {
        runBlocking {
            //Given
            val loggedInUser = User("1", "url", "dummyNick")
            appPreference.removeLoggedInUserId(loggedInUser.id)
            userDao.insert(loggedInUser)

            //When
            mainViewModel.checkForAutoLogin().join()

            //Then
            assert(appPreference.getLoggedInUserId() != loggedInUser.id)
            assert(userDao.getAllUsers().contains(loggedInUser))
            assert(mainViewModel.loggedInUser.value == null)
        }
    }

    @Test
    fun `checkForAutoLogin() autoLogin fail case when user is empty in roomDB`() {
        runBlocking {
            //Given
            val loggedInUser = User("1", "url", "dummyNick")
            appPreference.saveLoggedInUserId(loggedInUser.id)
            userDao.delete(loggedInUser)
            assert(userDao.getAllUsers().contains(loggedInUser).not())
            assert(appPreference.getLoggedInUserId() == loggedInUser.id)

            //When
            mainViewModel.checkForAutoLogin().join()

            //Then
            assert(appPreference.getLoggedInUserId() == loggedInUser.id)
            assert(userDao.getAllUsers().contains(loggedInUser).not())
            assert(mainViewModel.loggedInUser.value == null)
        }
    }

    @Test
    fun `checkForAutoLogin() autoLogin fail case when user is empty in roomDB and pref`() {
        runBlocking {
            //Given
            val loggedInUser = User("1", "url", "dummyNick")
            appPreference.removeLoggedInUserId(loggedInUser.id)
            userDao.delete(loggedInUser)

            //When
            mainViewModel.checkForAutoLogin().join()

            //Then
            assert(appPreference.getLoggedInUserId() != loggedInUser.id)
            assert(userDao.getAllUsers().contains(loggedInUser).not())
            assert(mainViewModel.loggedInUser.value == null)
        }
    }

    @Test
    fun checkNicknameError() {
        assert(mainViewModel.checkNicknameError("") == R.string.fill_in_the_blank)
        assert(mainViewModel.checkNicknameError(" ") == R.string.min_three_character_must_be_entered)
        assert(mainViewModel.checkNicknameError("  ") == R.string.min_three_character_must_be_entered)
        assert(mainViewModel.checkNicknameError("a") == R.string.min_three_character_must_be_entered)
        assert(mainViewModel.checkNicknameError("aa") == R.string.min_three_character_must_be_entered)
        assert(mainViewModel.checkNicknameError("      ") == null)
        assert(mainViewModel.checkNicknameError("    ss    ") == null)
        assert(mainViewModel.checkNicknameError("    rs") == null)
        assert(mainViewModel.checkNicknameError("sr     ") == null)
        assert(mainViewModel.checkNicknameError("    s    f") == null)
        assert(mainViewModel.checkNicknameError("s  s  ") == null)
        assert(mainViewModel.checkNicknameError("s    s") == null)
        assert(mainViewModel.checkNicknameError("test") == null)
        assert(mainViewModel.checkNicknameError("test test") == null)
        assert(mainViewModel.checkNicknameError("123") == null)
    }

    @Test
    fun verifyNicknameTrim() {
        assert(mainViewModel.verifyNicknameTrim("").not())
        assert(mainViewModel.errorNicknameResId.value == R.string.fill_in_the_blank)

        assert(mainViewModel.verifyNicknameTrim("      ").not())
        assert(mainViewModel.errorNicknameResId.value == R.string.fill_in_the_blank)

        assert(mainViewModel.verifyNicknameTrim("    ss    ").not())
        assert(mainViewModel.errorNicknameResId.value == R.string.min_three_character_must_be_entered)

        assert(mainViewModel.verifyNicknameTrim("    rs").not())
        assert(mainViewModel.errorNicknameResId.value == R.string.min_three_character_must_be_entered)

        assert(mainViewModel.verifyNicknameTrim("sr     ").not())
        assert(mainViewModel.errorNicknameResId.value == R.string.min_three_character_must_be_entered)

        assert(mainViewModel.verifyNicknameTrim("    s    f"))
        assert(mainViewModel.errorNicknameResId.value == null)

        assert(mainViewModel.verifyNicknameTrim("s  s  "))
        assert(mainViewModel.errorNicknameResId.value == null)

        assert(mainViewModel.verifyNicknameTrim("s    s"))
        assert(mainViewModel.errorNicknameResId.value == null)

        assert(mainViewModel.verifyNicknameTrim("test"))
        assert(mainViewModel.errorNicknameResId.value == null)

        assert(mainViewModel.verifyNicknameTrim("test test"))
        assert(mainViewModel.errorNicknameResId.value == null)

        assert(mainViewModel.verifyNicknameTrim("123"))
        assert(mainViewModel.errorNicknameResId.value == null)

    }

    @Test
    fun `successLogin() for New User`() {
        runBlocking {
            //Given
            val nickname = "newNickname"
            assert(userDao.getUserByNickname(nickname) == null)
            assert(appPreference.getLoggedInUserId() == null)

            //When
            mainViewModel.successLogin(nickname).join()

            //Then
            val loggedInUser = userDao.getUserByNickname(nickname)
            assert(mainViewModel.loggedInUser.value == loggedInUser)
            assert(loggedInUser != null)
            assert(appPreference.getLoggedInUserId() == loggedInUser!!.id)
        }
    }

    @Test
    fun `successLogin() for login from DB`() {
        val user = User(System.currentTimeMillis().toString(), "url", "dummynick-2")
        runBlocking {
            //Given
            userDao.insert(user)
            assert(userDao.getAllUsers().contains(user))
            assert(appPreference.getLoggedInUserId() == null)

            //When
            mainViewModel.successLogin(user.nickname).join()

            //Them
            assert(appPreference.getLoggedInUserId() == user.id)
            assert(mainViewModel.loggedInUser.value == user)
        }
    }

    @Test
    fun chooseUserFromDb() {
        val user1 = User("3", "url1", "dummy3")
        val user2 = User("4", "url2", "dummy4")
        val user3 = User("5", "url3", "dummy5")
        userDao.insert(user1)
        userDao.insert(user2)
        userDao.delete(user3)
        assert(mainViewModel.chooseUserFromDb(user1.nickname) == user1)
        assert(mainViewModel.chooseUserFromDb(user2.nickname) == user2)
        assert(mainViewModel.chooseUserFromDb(user3.nickname) == null)
    }

    @Test
    fun createNewUser() {
        //Given
        val nickname = "test"

        //When
        val user = mainViewModel.createNewUser(nickname)

        //Then
        assert(user != null)
        assert(user.nickname == nickname)
        assert(userDao.getUserById(user.id) == null)
    }

    @Test
    fun addNewUserToDb() {
        runBlocking {
            //Given
            val user = User("5", "url", "dummy5")
            //When
            mainViewModel.addNewUserToDb(user).join()

            //Then
            assert(userDao.getAllUsers().contains(user))
        }
    }

    @Test
    fun deleteDatabaseData() {
        runBlocking {
            //Given
            val user1 = User("1", "url", "dummy1")
            val user2 = User("2", "url", "dummy2")

            val message1 = Message("1", "text-1", System.currentTimeMillis(), user1)
            val message2 = Message("2", "text-2", System.currentTimeMillis(), user2)
            val message3 = Message("3", "text-3", System.currentTimeMillis(), user2)
            val message4 = Message("4", "text-3", System.currentTimeMillis(), user1)
            val message5 = Message("5", "text-4", System.currentTimeMillis(), user2)

            userDao.insert(listOf(user1, user2))
            messageDao.insert(listOf(message1, message2, message3, message4, message5))

            assert(userDao.getAllUsers().isNotEmpty())
            assert(messageDao.getAllMessages().isNotEmpty())

            //When
            mainViewModel.deleteDatabaseData().join()

            //Them
            assert(userDao.getAllUsers().isEmpty())
            assert(messageDao.getAllMessages().isEmpty())

        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }


}
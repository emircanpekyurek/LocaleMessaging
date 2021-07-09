package com.pekyurek.emircan.presentation.ui.main

import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pekyurek.emircan.R
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.db.dao.UserDao
import com.pekyurek.emircan.data.pref.AppPreference
import com.pekyurek.emircan.domain.model.response.user.User
import com.pekyurek.emircan.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userDao: UserDao,
    private val messageDao: MessageDao,
    private val appPreference: AppPreference
) : BaseViewModel() {

    val errorNicknameResId = MutableLiveData<@StringRes Int?>()
    val loggedInUser = MutableLiveData<User>()

    private val minimumCharacterLength = 3

    fun checkForAutoLogin() = viewModelScope.launch(Dispatchers.IO) {
        val lastLoggedInUserId = appPreference.getLoggedInUserId() ?: return@launch
        val lastLoggedUserFromDb = userDao.getUserById(lastLoggedInUserId) ?: return@launch
        loggedInUser.postValue(lastLoggedUserFromDb)
    }

    fun loginButtonClicked(nickname: String) {
        if (verifyNicknameTrim(nickname)) {
            successLogin(nickname)
        }
    }

    @VisibleForTesting
    fun verifyNicknameTrim(nickname: String): Boolean {
        val errorText = checkNicknameError(nickname.trim())
        errorNicknameResId.postValue(errorText)
        return errorText == null
    }

    @VisibleForTesting
    @StringRes
    fun checkNicknameError(nickname: String): Int? {
        return when {
            nickname.isEmpty() -> R.string.fill_in_the_blank
            nickname.length < minimumCharacterLength -> R.string.min_three_character_must_be_entered
            else -> null
        }
    }

    @VisibleForTesting
    fun successLogin(nickname: String) = viewModelScope.launch(Dispatchers.IO) {
        val chosenLoginUser: User = chooseUserFromDb(nickname) ?: run {
            val newUser = createNewUser(nickname)
            addNewUserToDb(newUser)
            return@run newUser
        }
        appPreference.saveLoggedInUserId(chosenLoginUser.id)
        loggedInUser.postValue(chosenLoginUser)
    }

    @VisibleForTesting
    fun chooseUserFromDb(nickname: String): User? {
        return userDao.getUserByNickname(nickname)
    }

    @VisibleForTesting
    fun createNewUser(nickname: String): User =
        User(System.currentTimeMillis().toString(), "", nickname)

    @VisibleForTesting
    fun addNewUserToDb(user: User) = viewModelScope.launch(Dispatchers.IO) {
        userDao.insert(user)
    }

    fun deleteDatabaseData() = viewModelScope.launch(Dispatchers.IO) {
        userDao.deleteAllUser()
        messageDao.deleteAllMessages()
    }
}
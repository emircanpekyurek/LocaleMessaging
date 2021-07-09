package com.pekyurek.emircan.presentation.ui.message

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pekyurek.emircan.data.db.dao.MessageDao
import com.pekyurek.emircan.data.pref.AppPreference
import com.pekyurek.emircan.domain.model.response.chatdata.Message
import com.pekyurek.emircan.domain.model.response.user.User
import com.pekyurek.emircan.domain.usecase.chatdata.ChatDataUseCase
import com.pekyurek.emircan.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val chatDataUseCase: ChatDataUseCase,
    private val messageDao: MessageDao,
    private val appPreference: AppPreference,
) : BaseViewModel() {

    val messageList = MutableLiveData<List<Message>>()
    val loggedInUser = MutableLiveData<User>()
    val postNewMessage = MutableLiveData<Message>()

    private val _loggedInUser: User? get() = loggedInUser.value

    val logout = MutableLiveData<User>()

    fun loadMessageList(user: User) = viewModelScope.launch(Dispatchers.IO) {
        loggedInUser.postValue(user)
        request(
            flow = chatDataUseCase.execute(),
            onSuccess = { chatData -> messageList.postValue(chatData.messages) },
            finishActivityAfterErrorPopup = true
        )
    }

    fun postNewMessage(messageText: String) {
        createNewMessage(messageText)?.let { message ->
            viewModelScope.launch(Dispatchers.IO) { messageDao.insert(message) }
            postNewMessage.postValue(message)
        }
    }

    @VisibleForTesting
    fun createNewMessage(message: String): Message? {
        if (message.isBlank()) return null
        val currentTime = System.currentTimeMillis()
        return Message(
            id = currentTime.toString(),
            text = message,
            timestamp = currentTime,
            user = _loggedInUser ?: return null
        )
    }

    fun logout() {
        _loggedInUser?.id?.let { loggedInUserId ->
            appPreference.removeLoggedInUserId(loggedInUserId)
            logout.postValue(_loggedInUser)
        }
    }
}
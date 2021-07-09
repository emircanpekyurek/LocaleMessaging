package com.pekyurek.emircan.presentation.ui.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.pekyurek.emircan.R
import com.pekyurek.emircan.databinding.ActivityMessageBinding
import com.pekyurek.emircan.domain.model.response.user.User
import com.pekyurek.emircan.presentation.core.extensions.showToast
import com.pekyurek.emircan.presentation.ui.base.BaseActivity
import com.pekyurek.emircan.presentation.ui.main.MainActivity
import com.pekyurek.emircan.presentation.ui.message.adapter.MessageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageActivity : BaseActivity<ActivityMessageBinding, MessageViewModel>() {

    override val layoutResId: Int = R.layout.activity_message

    override val viewModel: MessageViewModel by viewModels()

    private lateinit var loggedInUser: User

    private val messageAdapter by lazy { MessageAdapter(loggedInUser.id) }

    override fun getArgs() {
        loggedInUser = intent?.getParcelableExtra(ARG_LOGGED_IN_USER) ?: run {
            showToast(R.string.user_not_found)
            finish()
            return
        }
    }

    override fun initBinding() {
        super.initBinding()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        viewModel.loadMessageList(loggedInUser)
    }

    override fun initViews() {
        binding.rvMessages.adapter = messageAdapter
    }

    override fun setObservers() {
        super.setObservers()

        viewModel.messageList.observe(this) {
            messageAdapter.setAllMessage(it)
            scrollToBottom()
        }

        viewModel.postNewMessage.observe(this) {
            messageAdapter.addMessage(it)
            binding.sendMessageContainer.etMessage.setText("")
            smoothScrollToBottom()
        }

        viewModel.logout.observe(this) {
            finish()
            startActivity(MainActivity.newIntent(this))
        }
    }

    private fun scrollToBottom() {
        binding.rvMessages.scrollToPosition(messageAdapter.itemCount - 1)
    }

    private fun smoothScrollToBottom() {
        binding.rvMessages.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }

    companion object {
        private const val ARG_LOGGED_IN_USER = "ARG_LOGGED_IN_USER"

        fun newIntent(context: Context, loggedInUser: User): Intent {
            return Intent(context, MessageActivity::class.java).apply {
                putExtra(ARG_LOGGED_IN_USER, loggedInUser)
            }
        }
    }
}
package com.pekyurek.emircan.presentation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.pekyurek.emircan.R
import com.pekyurek.emircan.databinding.ActivityMainBinding
import com.pekyurek.emircan.presentation.ui.base.BaseActivity
import com.pekyurek.emircan.presentation.ui.message.MessageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResId: Int get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    override fun initBinding() {
        super.initBinding()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun onInit(savedInstanceState: Bundle?) {
        super.onInit(savedInstanceState)
        viewModel.checkForAutoLogin()
    }

    override fun setObservers() {
        super.setObservers()
        viewModel.loggedInUser.observe(this) { loggedInUser ->
            startActivity(MessageActivity.newIntent(this, loggedInUser))
            finish()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }

}
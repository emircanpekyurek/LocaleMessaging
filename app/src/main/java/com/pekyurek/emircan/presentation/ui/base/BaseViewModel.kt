package com.pekyurek.emircan.presentation.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pekyurek.emircan.domain.exception.base.BaseException
import com.pekyurek.emircan.data.repository.ResultStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

abstract class BaseViewModel : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val showErrorPopup = MutableLiveData<Pair<BaseException, Boolean>>()

    protected fun showLoading(visibility: Boolean) = loading.postValue(visibility)

    suspend fun <T> request(
        flow: Flow<ResultStatus<T>>,
        onSuccess: ((data: T) -> Unit)? = null,
        onError: ((t: BaseException) -> Unit)? = null,
        forceLoadingHidden: Boolean = false,
        errorPopupCanVisible: Boolean = true,
        finishActivityAfterErrorPopup: Boolean = false
    ) {
        flow.collect { result ->
            when (result) {
                is ResultStatus.Loading -> showLoading(result.show && forceLoadingHidden.not())
                is ResultStatus.Success -> onSuccess?.invoke(result.data)
                is ResultStatus.Exception -> {
                    if (errorPopupCanVisible) {
                        showErrorPopup.postValue(Pair(result.exception, finishActivityAfterErrorPopup))
                    }
                    onError?.invoke(result.exception)
                }
            }
        }
    }
}
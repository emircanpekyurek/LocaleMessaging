package com.pekyurek.emircan.data.repository

import com.pekyurek.emircan.domain.exception.base.BaseException


sealed class ResultStatus<out Response> {

    data class Success<Data>(val data: Data) : ResultStatus<Data>()

    data class Exception(val exception: BaseException) : ResultStatus<Nothing>()

    data class Loading(val show: Boolean) : ResultStatus<Nothing>()

}
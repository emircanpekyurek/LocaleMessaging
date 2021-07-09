package com.pekyurek.emircan.domain.exception.service

import android.content.Context
import com.pekyurek.emircan.R
import com.pekyurek.emircan.domain.exception.ServiceException

class FailResponseException(context: Context, responseStatus: Int) :
    ServiceException(
        context.getString(
            R.string.exception_service_fail_response_with_status,
            responseStatus
        )
    )
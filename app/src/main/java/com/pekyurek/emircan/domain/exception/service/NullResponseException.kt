package com.pekyurek.emircan.domain.exception.service

import android.content.Context
import com.pekyurek.emircan.R
import com.pekyurek.emircan.domain.exception.ServiceException

class NullResponseException(context: Context) :
    ServiceException(
        context.getString(R.string.exception_service_null_response)
    )
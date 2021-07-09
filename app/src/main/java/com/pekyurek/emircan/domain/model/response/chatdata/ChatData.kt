package com.pekyurek.emircan.domain.model.response.chatdata

import com.pekyurek.emircan.domain.model.base.BaseResponse
import com.squareup.moshi.Json

data class ChatData(
    @Json(name = "messages")
    val messages: List<Message>
) : BaseResponse()
package com.pekyurek.emircan.presentation.core.extensions

import java.text.SimpleDateFormat
import java.util.*


fun Long.toMessageTimeFormat(): String {
    return SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault()).format(this)
}
package com.pekyurek.emircan.data.pref

interface PreferenceRepository {

    fun saveLoggedInUserId(id: String)

    fun getLoggedInUserId(): String?

    fun removeLoggedInUserId(id: String)
}
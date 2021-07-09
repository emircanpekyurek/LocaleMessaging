package com.pekyurek.emircan.data.pref

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class AppPreference @Inject constructor(context: Context) : PreferenceRepository {

    private val pref by lazy { context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    override fun saveLoggedInUserId(id: String) {
        pref.edit { putString(KEY_LOGGED_IN_USER_ID, id) }
    }

    override fun getLoggedInUserId(): String? {
        return pref.getString(KEY_LOGGED_IN_USER_ID, null)
    }

    override fun removeLoggedInUserId(id: String) {
        pref.edit { remove(KEY_LOGGED_IN_USER_ID) }
    }

    companion object {
        private const val PREF_NAME = "APP_PREF"

        private const val KEY_LOGGED_IN_USER_ID = "KEY_LOGGED_IN_USER_ID"
    }

}
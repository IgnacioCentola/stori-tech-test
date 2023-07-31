package com.nacho.data.preferences

import android.content.SharedPreferences
import com.nacho.domain.preferences.Preferences


class DefaultPreferences(
    private val sharedPreferences: SharedPreferences
) : Preferences {
    override fun saveUserId(userId: String) {
        sharedPreferences
            .edit()
            .putString(Preferences.KEY_USER_ID, userId)
            .apply()
    }

    override fun getUserId(): String {
        return sharedPreferences.getString(Preferences.KEY_USER_ID,"").orEmpty()
    }
}
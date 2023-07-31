package com.nacho.domain.preferences

interface Preferences {
    fun saveUserId(userId: String)
    fun getUserId(): String

    companion object{
        const val KEY_USER_ID = "user_id"
    }
}
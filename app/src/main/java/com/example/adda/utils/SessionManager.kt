package com.example.adda.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SessionManager @SuppressLint("CommitPrefEdits") constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    var isLogedIn: Boolean
        get() = sharedPreferences.getBoolean(isLogedInKey, false)
        set(code) {
            editor.putBoolean(isLogedInKey, code).apply()
        }

    var userIdToken: String?
        get() = sharedPreferences.getString(userIdKey, "")
        set(code) {
            editor.putString(userIdKey, code).apply()
        }

    companion object {
        private const val Pref_Name = "adda_dei"
        private const val isLogedInKey = "isLogedIn"
        private const val userIdKey = "userId"
    }

    fun clearSP() {
        editor.remove(isLogedInKey).apply()
        editor.putBoolean(isLogedInKey, false).apply()


    }
}
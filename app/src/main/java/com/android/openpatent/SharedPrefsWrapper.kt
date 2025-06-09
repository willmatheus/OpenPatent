package com.android.openpatent

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPrefsWrapper(context: Context) {

    companion object {
        private const val PREFS_NAME = "OPEN_PATENT_PREFS"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) {
        sharedPrefs.edit { putString(key, value) }
    }

    fun getString(key: String): String? {
        return sharedPrefs.getString(key, "")
    }

    fun putInt(key: String, value: Int) {
        sharedPrefs.edit { putInt(key, value) }
    }

    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPrefs.getBoolean(key, false)
    }

    fun remove(key: String) {
        sharedPrefs.edit { remove(key) }
    }

    fun clear() {
        sharedPrefs.edit { clear() }
    }
}

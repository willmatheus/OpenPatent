package com.android.openpatent

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsWrapper(context: Context) {

    companion object {
        private const val PREFS_NAME = "my_app_prefs"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // Salvar uma String
    fun putString(key: String, value: String) {
        sharedPrefs.edit().putString(key, value).apply()
    }

    // Recuperar uma String
    fun getString(key: String, defaultValue: String? = null): String? {
        return sharedPrefs.getString(key, defaultValue)
    }

    // Salvar um Int
    fun putInt(key: String, value: Int) {
        sharedPrefs.edit().putInt(key, value).apply()
    }

    // Recuperar um Int
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return sharedPrefs.getInt(key, defaultValue)
    }

    // Salvar um Boolean
    fun putBoolean(key: String, value: Boolean) {
        sharedPrefs.edit().putBoolean(key, value).apply()
    }

    // Recuperar um Boolean
    fun getBoolean(key: String): Boolean {
        return sharedPrefs.getBoolean(key, false)
    }

    // Remover um valor
    fun remove(key: String) {
        sharedPrefs.edit().remove(key).apply()
    }

    // Limpar todos os dados
    fun clear() {
        sharedPrefs.edit().clear().apply()
    }
}

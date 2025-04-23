package com.android.openpatent.repository

import android.content.Context
import com.android.openpatent.SharedPrefsWrapper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val sharedPrefs = SharedPrefsWrapper(context)

    fun saveUsername(username: String) {
        sharedPrefs.putString("USERNAME", username)
    }

    fun saveLoginStatus(loginStatus: Boolean) {
        sharedPrefs.putBoolean("IS_USER_LOGGED", loginStatus)
    }

    fun isUserLogged() = sharedPrefs.getBoolean("IS_USER_LOGGED")

    fun getUsername() = sharedPrefs.getString("USERNAME")
}
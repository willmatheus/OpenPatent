package com.android.openpatent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.openpatent.screens.RegisterUserScreen
import com.android.openpatent.ui.theme.OpenPatentTheme
import com.android.openpatent.viewmodel.UserViewModel

class UserAuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = SharedPrefsWrapper(applicationContext)
        if (sharedPrefs.getBoolean("IS_USER_LOGGED")) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAndRemoveTask()
        } else {
            setContent {
                OpenPatentTheme {
                    RegisterUserScreen(UserViewModel(), {
                        sharedPrefs.putBoolean("IS_USER_LOGGED", true)
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAndRemoveTask()
                    }, { finishAndRemoveTask() })
                }
            }
        }
    }
}
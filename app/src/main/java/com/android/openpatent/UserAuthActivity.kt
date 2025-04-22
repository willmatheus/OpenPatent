package com.android.openpatent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.openpatent.repository.UserRepository
import com.android.openpatent.screens.RegisterUserScreen
import com.android.openpatent.ui.theme.OpenPatentTheme
import com.android.openpatent.viewmodel.UserViewModel

class UserAuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userRepository = UserRepository(applicationContext)
        if (userRepository.isUserLogged()) {
            startActivity(Intent(this, MainActivity::class.java))
            finishAndRemoveTask()
        } else {
            setContent {
                OpenPatentTheme {
                    RegisterUserScreen(UserViewModel(userRepository), {
                        startActivity(Intent(this, MainActivity::class.java))
                        finishAndRemoveTask()
                    }, { finishAndRemoveTask() })
                }
            }
        }
    }
}
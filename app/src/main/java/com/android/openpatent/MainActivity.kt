package com.android.openpatent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.openpatent.repository.UserRepository
import com.android.openpatent.screens.MainScreen
import com.android.openpatent.ui.theme.OpenPatentTheme
import com.android.openpatent.viewmodel.PatentViewModel

class MainActivity : ComponentActivity() {

    private lateinit var patentViewModel: PatentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userRepository = UserRepository(applicationContext)
        patentViewModel = PatentViewModel(userRepository)
        enableEdgeToEdge()
        setContent {
            OpenPatentTheme {
                MainScreen(this, patentViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        patentViewModel.getPatents()
    }
}



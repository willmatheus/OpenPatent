package com.android.openpatent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.android.openpatent.screens.AppNavigation
import com.android.openpatent.ui.theme.OpenPatentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val patentViewModel: HiltPatentViewModel by viewModels()
    private val userViewModel: HiltUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenPatentTheme {
                AppNavigation(patentViewModel, userViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        patentViewModel.getPatents()
    }
}



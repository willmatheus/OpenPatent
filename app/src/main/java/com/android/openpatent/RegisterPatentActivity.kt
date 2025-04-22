package com.android.openpatent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.openpatent.screens.AppNavigation
import com.android.openpatent.ui.theme.OpenPatentTheme
import com.android.openpatent.viewmodel.PatentViewModel

class RegisterPatentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenPatentTheme {
                AppNavigation(PatentViewModel(), this)
            }
        }
    }
}
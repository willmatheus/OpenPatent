package com.android.openpatent.screens

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.openpatent.SharedPrefsWrapper
import com.android.openpatent.viewmodel.PatentViewModel
import com.android.openpatent.viewmodel.UserViewModel

@Composable
fun AppNavigation(patentViewModel: PatentViewModel, userViewModel: UserViewModel, activity: Activity) {
    val navController = rememberNavController()
    val patentUiState = patentViewModel.uiState.collectAsStateWithLifecycle().value
    val userUiState = userViewModel.uiState.collectAsStateWithLifecycle().value
    val username = SharedPrefsWrapper(activity.applicationContext).getString("USERNAME")!!
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, patentUiState, userUiState) }
        composable("register") { RegisterPatentScreen(patentUiState, navController, username) }
        composable("loading") { LoadingScreen(navController) }
        composable("user-patents") { UserPatentsScreen(navController, userUiState) }
    }
}
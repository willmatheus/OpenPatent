package com.android.openpatent.screens

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.openpatent.viewmodel.PatentViewModel

@Composable
fun AppNavigation(patentViewModel: PatentViewModel, activity: Activity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "register") {
        composable("register") { RegisterPatentScreen(patentViewModel, navController) }
        composable("loading") { LoadingScreen(activity) }
    }
}
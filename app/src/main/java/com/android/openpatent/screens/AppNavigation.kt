package com.android.openpatent.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.openpatent.viewmodel.PatentViewModel

@Composable
fun AppNavigation(patentViewModel: PatentViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController, patentViewModel) }
        composable("register") { RegisterPatentScreen(patentViewModel, navController) }
        composable("loading") { LoadingScreen(navController) }
        composable("user-patents") { UserPatentsScreen(navController, patentViewModel) }
    }
}
package com.android.openpatent.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoadingScreen(navController: NavController) {
    var isLoading by remember { mutableStateOf(true) }
    var isSuccess by remember { mutableStateOf(false) }
    var shouldNavigate by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        isLoading = false
        isSuccess = true
        kotlinx.coroutines.delay(2000)
        shouldNavigate = true
    }

    // Navega para MainActivity após mostrar sucesso
    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            navController.navigate("main")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(modifier = Modifier.size(50.dp))
            }
            isSuccess -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Sucesso",
                        modifier = Modifier.size(100.dp),
                        tint = Color.Green
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Patente registrada!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


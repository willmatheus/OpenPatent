package com.android.openpatent.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.android.openpatent.RegisterPatentActivity
import com.android.openpatent.data.PatentData
import com.android.openpatent.viewmodel.PatentViewModel
import kotlin.math.roundToInt

@Composable
fun MainScreen(activity: Activity, viewModel: PatentViewModel) {
    val patentList by viewModel.patents.collectAsState()
    val drawerWidth = 300.dp
    val drawerWidthPx = with(LocalDensity.current) { drawerWidth.toPx() }

    var isDrawerOpen by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(-drawerWidthPx) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = if (isDrawerOpen) 0f else -drawerWidthPx,
        label = "drawer_offset"
    )

    val selectedPatent = remember { mutableStateOf<PatentData?>(null) }
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getPatents()
    }

    val paddingValues = WindowInsets.systemBars.asPaddingValues()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    offsetX += dragAmount
                    offsetX = offsetX.coerceIn(-drawerWidthPx, 0f)
                    if (dragAmount > 30) isDrawerOpen = true
                    if (dragAmount < -30) isDrawerOpen = false
                }
            }
    ) {
        // Conteúdo principal
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    "Patentes registradas",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                patentList.forEach { block ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedPatent.value = block
                                showDialog.value = true
                            },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Título: ${block.title}")
                            Text("Inventor: ${block.inventor}")
                        }
                    }
                }

                if (showDialog.value && selectedPatent.value != null) {
                    val patent = selectedPatent.value!!
                    AlertDialog(
                        onDismissRequest = {
                            showDialog.value = false
                            selectedPatent.value = null
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog.value = false
                                selectedPatent.value = null
                            }) {
                                Text("Fechar")
                            }
                        },
                        title = {
                            Text("Detalhes da Patente")
                        },
                        text = {
                            Column {
                                Text("Título: ${patent.title}")
                                Text("Inventor: ${patent.inventor}")
                                Text("Descrição: ${patent.description}")
                            }
                        },
                        properties = DialogProperties(dismissOnClickOutside = true)
                    )
                }
            }
        }

        // Drawer lateral
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(drawerWidth)
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .background(MaterialTheme.colorScheme.primaryContainer)
                .align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Registrar",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Button(onClick = {
                    switchToRegisterActivity(activity)
                    isDrawerOpen = false
                }) {
                    Text("Registrar nova patente")
                }
            }
        }

        // FAB ajustado com navigationBarsPadding
        FloatingActionButton(
            onClick = { switchToRegisterActivity(activity) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .navigationBarsPadding(),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Novo", tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

fun switchToRegisterActivity(activity: Activity) {
    val intent = Intent(activity, RegisterPatentActivity::class.java)
    activity.startActivity(intent)
}
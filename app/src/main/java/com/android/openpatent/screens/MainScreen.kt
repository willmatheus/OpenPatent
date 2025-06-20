package com.android.openpatent.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.PatentUiState
import com.android.openpatent.data.UserUiState
import kotlin.math.roundToInt

@Composable
fun MainScreen(navController: NavController, uiState: PatentUiState, userUiState: UserUiState) {
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
        uiState.onLaunch()
        userUiState.onLaunch()
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Patentes registradas",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp)) // Espaço manual entre os itens

                    Box(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 120.dp) // largura mínima
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Carteira: 0.0 SepoliaETH",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp)
                        )
                    }
                } else {
                    uiState.getAllPatents.forEach { block ->
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
                                Text("Data de registro: ${block.registrationDate}")
                                Text("Inventor: ${block.inventor}")
                            }
                        }
                    }
                }

                if (showDialog.value && selectedPatent.value != null) {
                    val patent = selectedPatent.value!!
                    val showConfirmDialog = remember { mutableStateOf(false) }

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
                        dismissButton = {
                            TextButton(onClick = {
                                showConfirmDialog.value = true
                            }) {
                                Text("Comprar patente")
                            }
                        },
                        title = {
                            Text("Detalhes da Patente")
                        },
                        text = {
                            Column {
                                Text("Título: ${patent.title}")
                                Text("Inventor: ${patent.inventor}")
                                Text("Data de registro: ${patent.registrationDate}")
                                Text("Descrição: ${patent.description}")
                                Text("Preco: ${patent.price} Bytecoins")
                            }
                        },
                        properties = DialogProperties(dismissOnClickOutside = true)
                    )

                    if (showConfirmDialog.value) {
                        AlertDialog(
                            onDismissRequest = { showConfirmDialog.value = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    // Ação ainda não implementada
                                    uiState.onBuyPatent(patent)
                                    showConfirmDialog.value = false
                                    navController.navigate("loading", null)
                                }) {
                                    Text("Sim")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    showConfirmDialog.value = false
                                }) {
                                    Text("Cancelar")
                                }
                            },
                            title = {
                                Text("Confirmar compra")
                            },
                            text = {
                                Text("Você tem certeza que deseja comprar esta patente?")
                            },
                            properties = DialogProperties(dismissOnClickOutside = true)
                        )
                    }
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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Registrar",
                    modifier = Modifier
                        .size(64.dp)
                        .padding(bottom = 24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Button(
                    onClick = {
                        navController.navigate("register")
                        isDrawerOpen = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("Registrar nova patente")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("user-patents")
                        isDrawerOpen = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Text("Minhas patentes")
                }
            }
        }

        // FAB ajustado com navigationBarsPadding
        FloatingActionButton(
            onClick = { navController.navigate("register") },
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
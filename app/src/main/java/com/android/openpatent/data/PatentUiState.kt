package com.android.openpatent.data

data class PatentUiState(
    val onLaunch: () -> Unit = {},
    val onRegisterPatent: (RegisterPatent) -> Unit = {},
    val onBuyPatent: (PatentData) -> Unit = {},
    val isPatentRegistered: Boolean = false,
    val isPatentPurchased: Boolean = false,
    val isLoading: Boolean = true,
    val getAllPatents: List<PatentData>,
)
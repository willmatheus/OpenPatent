package com.android.openpatent.data

data class UserUiState(
    val onLaunch: () -> Unit,
    val onRegisterUser: (CreateUserData) -> Unit = {},
    val isUserRegistered: Boolean = false,
    val isLoading: Boolean = true,
    val userPatents: List<PatentData> = emptyList(),
    val userWallet: Double = 0.0
)

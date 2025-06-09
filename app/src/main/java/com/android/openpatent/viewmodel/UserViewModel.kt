package com.android.openpatent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.openpatent.data.CreateUserData
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.UserUiState
import com.android.openpatent.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UserUiState(
            onLaunch = ::onLaunch,
            onRegisterUser = ::onRegisterUser,
            userPatents = emptyList()
        )
    )

    val uiState = _uiState.asStateFlow()

    private fun onLaunch() {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy(
                    userPatents = getUserPatents(),
                    userWallet = getUserWallet(),
                    isLoading = false
                )
            }
        }
    }

    private fun onRegisterUser(createUserData: CreateUserData) {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy(
                    isUserRegistered = registerUser(createUserData)
                )
            }
        }
    }

    private suspend fun getUserPatents() : List<PatentData> =
        userRepository.getUserPatents()

    private suspend fun registerUser(createUserData: CreateUserData) : Boolean =
        userRepository.registerUser(createUserData)

    private suspend fun getUserWallet() : Double =
        userRepository.getUserWallet()
}

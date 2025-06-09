package com.android.openpatent.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.PatentUiState
import com.android.openpatent.data.RegisterPatent
import com.android.openpatent.repository.PatentRepository
import com.android.openpatent.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class PatentViewModel(
    private val userRepository: UserRepository,
    private val patentRepository: PatentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PatentUiState(
            onLaunch = ::loadAllPatents,
            onRegisterPatent = ::onRegisterPatent,
            onBuyPatent = ::onBuyPatent,
            getAllPatents = emptyList(),
        )
    )

    val uiState = _uiState.asStateFlow()

    private fun loadAllPatents() {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy (
                    getAllPatents = getAllPatents(),
                    isLoading = false
                )
            }
        }
    }

    private fun onRegisterPatent(patent: RegisterPatent) {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy (
                    isPatentRegistered = registerPatent(patent),
                    isLoading = false
                )
            }
        }
    }

    private fun onBuyPatent(patent: PatentData) {
        viewModelScope.launch {
            _uiState.update { update ->
                update.copy (
                    isPatentPurchased = buyPatent(patent),
                    isLoading = false
                )
            }
        }
    }

    private suspend fun registerPatent(patent: RegisterPatent) : Boolean =
        patentRepository.registerPatent(patent)

    private suspend fun getAllPatents() : List<PatentData> =
        patentRepository.getAllPatents()

    private suspend fun buyPatent(patent: PatentData) : Boolean {
        val username = userRepository.getUsername()!!
        return patentRepository.buyPatent(patent, username)
    }
}
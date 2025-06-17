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
import org.web3j.protocol.Web3j
import io.metamask.androidsdk.Ethereum
import org.web3j.protocol.http.HttpService
import java.math.BigInteger

abstract class UserViewModel(
    private val userRepository: UserRepository,
    private val ethereum: Ethereum // Recebido por injeção ou manualmente
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        UserUiState(
            onLaunch = ::onLaunch,
            onRegisterUser = ::onRegisterUser,
            onConnectWallet = ::connectToMetaMask, // novo callback
            userPatents = emptyList()
        )
    )

    val uiState = _uiState.asStateFlow()

    private var walletAddress: String = ""
    private var web3j: Web3j? = null

    private val gasLimit = BigInteger.valueOf(300_000)
    private val gasPrice = BigInteger.valueOf(20_000_000_000L)
    private val infuraUrl = "https://sepolia.infura.io/v3/58a3c6e99e634500a03a4ee09261e8a0"

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
                    isUserRegistered = registerUser(createUserData),
                    isLoading = false,
                )
            }
        }
    }

    fun connectToMetaMask(onError: (String) -> Unit = {}, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                ethereum.connect {
                    val address = ethereum.selectedAddress
                    if (address.isBlank()) {
                        onError("Carteira MetaMask não conectada.")
                    } else {
                        walletAddress = address
                        initWeb3()
                        _uiState.update { it.copy(userWalletAddress = address) }
                        onSuccess()
                    }
                }
            } catch (e: Exception) {
                onError("Erro ao conectar com a MetaMask: ${e.message}")
            }
        }
    }

    private fun initWeb3() {
        if (web3j == null) {
            val httpService = HttpService(infuraUrl)
            web3j = Web3j.build(httpService)
        }
    }

    private suspend fun getUserPatents(): List<PatentData> =
        userRepository.getUserPatents()

    private suspend fun registerUser(createUserData: CreateUserData): Boolean =
        userRepository.registerUser(createUserData)

    private suspend fun getUserWallet(): Double =
        userRepository.getUserWallet()
}

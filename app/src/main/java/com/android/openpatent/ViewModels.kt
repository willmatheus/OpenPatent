package com.android.openpatent

import com.android.openpatent.repository.PatentRepository
import com.android.openpatent.repository.UserRepository
import com.android.openpatent.viewmodel.PatentViewModel
import com.android.openpatent.viewmodel.UserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.metamask.androidsdk.Ethereum
import javax.inject.Inject

@HiltViewModel
class HiltUserViewModel @Inject constructor(
    userRepository: UserRepository,
    ethereum: Ethereum
) : UserViewModel(userRepository, ethereum)

@HiltViewModel
class HiltPatentViewModel @Inject constructor(
    userRepository: UserRepository,
    patentRepository: PatentRepository
) : PatentViewModel(userRepository, patentRepository)
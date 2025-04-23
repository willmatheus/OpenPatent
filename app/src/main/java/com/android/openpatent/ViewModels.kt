package com.android.openpatent

import com.android.openpatent.repository.UserRepository
import com.android.openpatent.viewmodel.PatentViewModel
import com.android.openpatent.viewmodel.UserViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HiltUserViewModel @Inject constructor(
    userRepository: UserRepository
) : UserViewModel(userRepository)

@HiltViewModel
class HiltPatentViewModel @Inject constructor(
    userRepository: UserRepository
) : PatentViewModel(userRepository)
package com.android.openpatent.data

data class UserData(
    val name: String,
    val username: String,
    val cpf: String,
    val password: String,
    val userPatents: List<PatentData>,
    val wallet: Double
)

data class CreateUserData(
    val name: String,
    val username: String,
    val cpf: String,
    val password: String,
)

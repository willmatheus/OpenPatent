package com.android.openpatent.data

data class PatentData(
    val inventor: String,
    val title: String,
    val description: String,
    val price: Double,
    val registrationDate: String
)

data class RegisterPatent(
    val inventor: String,
    val title: String,
    val description: String,
    val price: Double,
)
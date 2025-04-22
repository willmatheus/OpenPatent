package com.android.openpatent.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class UserData(
    val name: String,
    val username: String,
    val cpf: String,
    val password: String,
)

data class PatentData(
    val inventor: String,
    val title: String,
    val description: String
)

interface IPatentNetwork {

    // Usuários
    @POST("blockchain/register-user")
    fun registerUser(@Body user: UserData): Call<ApiResponse>

    @POST("blockchain/login")
    fun login(@Body loginData: UserData): Call<String>

    @GET("blockchain/users")
    fun getAllUsers(): Call<List<UserData>>

    // Patentes
    @POST("blockchain/register-patent")
    fun registerPatent(@Body patent: PatentData): Call<String>

    @GET("blockchain/patents")
    fun getAllPatents(): Call<List<PatentData>>

    // Blockchain
    @GET("blockchain/validate")
    fun validarBlockchain(): Call<String>
}

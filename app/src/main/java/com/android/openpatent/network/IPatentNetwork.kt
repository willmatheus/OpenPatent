package com.android.openpatent.network

import com.android.openpatent.data.CreateUserData
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ApiResponse(
    val success: Boolean,
    val message: String
)

interface IPatentNetwork {

    // Usuários
    @POST("blockchain/register-user")
    fun registerUser(@Body user: CreateUserData): Call<ApiResponse>

    @POST("blockchain/login")
    fun login(@Body loginData: UserData): Call<String>

    @GET("blockchain/users")
    fun getAllUsers(): Call<List<UserData>>

    // Patentes
    @POST("blockchain/register-patent")
    fun registerPatent(@Body patent: PatentData): Call<String>

    @GET("blockchain/patents")
    fun getAllPatents(): Call<List<PatentData>>

    @POST("blockchain/user-patents")
    fun getUserPatents(@Body username: String): Call<List<PatentData>>

    // Blockchain
    @GET("blockchain/validate")
    fun validarBlockchain(): Call<String>
}

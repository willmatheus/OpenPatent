package com.android.openpatent.network

import com.android.openpatent.data.CreateUserData
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.RegisterPatent
import com.android.openpatent.data.UserData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ApiResponse(
    val success: Boolean,
    val message: String
)

data class BuyPatent(
    val patent: PatentData,
    val username: String
)

interface IPatentNetwork {

    // Usuários
    @POST("blockchain/register-user")
    fun registerUser(@Body user: CreateUserData): Call<ApiResponse>

    @POST("blockchain/login")
    fun login(@Body loginData: UserData): Call<String>

    @GET("blockchain/users")
    fun getAllUsers(): Call<List<UserData>>

    @POST("blockchain/user")
    fun getUser(@Body username: String): Call<UserData>

    @POST("blockchain/register-patent")
    fun registerPatent(@Body patent: RegisterPatent): Call<String>

    @POST("blockchain/buy-patent")
    fun buyPatent(@Body buyPatent: BuyPatent): Call<Boolean>

    @GET("blockchain/patents")
    suspend fun getAllPatents(): List<PatentData>

    @POST("blockchain/user-patents")
    fun getUserPatents(@Body username: String): Call<List<PatentData>>

    // Blockchain
    @GET("blockchain/validate")
    fun validarBlockchain(): Call<String>
}

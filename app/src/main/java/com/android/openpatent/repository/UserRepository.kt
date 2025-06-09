package com.android.openpatent.repository

import android.content.Context
import android.util.Log
import com.android.openpatent.SharedPrefsWrapper
import com.android.openpatent.data.CreateUserData
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.UserData
import com.android.openpatent.network.ApiResponse
import com.android.openpatent.network.RetrofitService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    private val call = RetrofitService.api
    private val sharedPrefs = SharedPrefsWrapper(context)

    fun saveUsername(username: String) {
        sharedPrefs.putString("USERNAME", username)
    }

    fun saveLoginStatus(loginStatus: Boolean) {
        sharedPrefs.putBoolean("IS_USER_LOGGED", loginStatus)
    }

    fun isUserLogged() = sharedPrefs.getBoolean("IS_USER_LOGGED")

    fun getUsername() = sharedPrefs.getString("USERNAME")

    suspend fun getUserPatents() = withContext(Dispatchers.IO) {
        var userPatentList: List<PatentData> = emptyList()
        call.getUserPatents(getUsername()!!).enqueue(object : Callback<List<PatentData>> {
            override fun onResponse(
                call: Call<List<PatentData>>,
                response: Response<List<PatentData>>
            ) {
                Log.d("getUserPatents", "response: ${getUsername()!!}")
                if (response.isSuccessful) {
                    userPatentList = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<PatentData>>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
        userPatentList
    }

    suspend fun registerUser(createUserData: CreateUserData) : Boolean = withContext(Dispatchers.IO) {
        var isUserRegistered = false
        call.registerUser(createUserData).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("UserViewModel", "Mensagem: ${body?.message}")
                    saveUsername(createUserData.username)
                    saveLoginStatus(true)
                    isUserRegistered = true
                } else {
                    Log.e("UserViewModel", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("UserViewModel", "Erro: ${t.message}")
            }
        })
        isUserRegistered
    }

    suspend fun getUserWallet() = withContext(Dispatchers.IO) {
        var wallet = 0.0
        call.getUser(getUsername()!!).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if (response.isSuccessful) {
                    wallet = response.body()?.wallet ?: 0.0

                } else {
                    Log.e("UserViewModel", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("UserViewModel", "Erro: ${t.message}")
            }
        })
        wallet
    }
}
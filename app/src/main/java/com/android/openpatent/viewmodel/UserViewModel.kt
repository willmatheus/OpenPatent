package com.android.openpatent.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.openpatent.network.ApiResponse
import com.android.openpatent.network.UserData
import com.android.openpatent.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val api = RetrofitService.api

    fun registerUser(name: String, username: String, cpf: String, password: String, onResult: (Boolean) -> Unit) {
        val user = UserData(name, username, cpf, password)
        api.registerUser(user).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("UserViewModel", "Mensagem: ${body?.message}")
                    onResult(body?.success == true)
                } else {
                    Log.e("UserViewModel", "Erro na resposta: ${response.errorBody()?.string()}")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e("UserViewModel", "Erro: ${t.message}")
                onResult(false)
            }
        })
    }
}

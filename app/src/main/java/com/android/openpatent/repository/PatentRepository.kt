package com.android.openpatent.repository

import android.content.Context
import android.util.Log
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.RegisterPatent
import com.android.openpatent.network.BuyPatent
import com.android.openpatent.network.RetrofitService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class PatentRepository@Inject constructor(
    @ApplicationContext context: Context
) {
    private val call = RetrofitService.api

    suspend fun getAllPatents() = withContext(Dispatchers.IO) {
        var patentList: List<PatentData> = emptyList()
        call.getAllPatents().enqueue(object : Callback<List<PatentData>> {
            override fun onResponse(
                call: Call<List<PatentData>>,
                response: Response<List<PatentData>>
            ) {
                if (response.isSuccessful) {
                    patentList = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<PatentData>>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
        patentList
    }

    suspend fun registerPatent(patent: RegisterPatent) : Boolean = withContext(Dispatchers.IO) {
        var isPatentRegistered = false
        var message = ""
        call.registerPatent(patent).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                message = response.body() ?: "Erro desconhecido"
                isPatentRegistered = response.isSuccessful
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                message = "Falha: ${t.localizedMessage}"
            }
        })
        isPatentRegistered
    }

    suspend fun buyPatent(patent: PatentData, username: String) = withContext(Dispatchers.IO) {
        val buyPatent = BuyPatent(patent, username)
        var isPatentPurchased = false
        Log.e("PatentViewModel", "PatentData: $patent")
        call.buyPatent(buyPatent).enqueue(object : Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                isPatentPurchased = response.isSuccessful
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
        isPatentPurchased
    }
}
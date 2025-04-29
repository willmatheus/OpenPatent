package com.android.openpatent.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.openpatent.data.PatentData
import com.android.openpatent.data.RegisterPatent
import com.android.openpatent.network.BuyPatent
import com.android.openpatent.network.RetrofitService
import com.android.openpatent.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class PatentViewModel(private val userRepository: UserRepository) : ViewModel() {

    var message: String = ""
    private val call = RetrofitService.api

    private val _patents = MutableStateFlow<List<PatentData>>(emptyList())
    val patents: StateFlow<List<PatentData>> = _patents

    private val _user_patents = MutableStateFlow<List<PatentData>>(emptyList())
    val user_patents: StateFlow<List<PatentData>> = _user_patents

    private val _is_patent_accquired = MutableStateFlow(false)
    val is_patent_accquired: StateFlow<Boolean> = _is_patent_accquired

    fun registerPatent(title: String, description: String, price: Double, onResult: (Boolean) -> Unit) {
        val inventorUsername = userRepository.getUsername()!!
        val request = RegisterPatent(inventorUsername, title, description, price)

        call.registerPatent(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                message = response.body() ?: "Erro desconhecido"
                onResult(true)
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                message = "Falha: ${t.localizedMessage}"
                onResult(false)
            }
        })
    }

    fun getPatents() {
        call.getAllPatents().enqueue(object : Callback<List<PatentData>> {
            override fun onResponse(
                call: Call<List<PatentData>>,
                response: Response<List<PatentData>>
            ) {
                if (response.isSuccessful) {
                    _patents.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<PatentData>>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
    }

    fun getUserPatents() {
        val username = userRepository.getUsername()!!
        call.getUserPatents(username).enqueue(object : Callback<List<PatentData>> {
            override fun onResponse(
                call: Call<List<PatentData>>,
                response: Response<List<PatentData>>
            ) {
                Log.d("getUserPatents", "response: $username")
                if (response.isSuccessful) {
                    _user_patents.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<PatentData>>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
    }

    fun buyPatent(patent: PatentData) {
        val username = userRepository.getUsername()!!
        val buyPatent = BuyPatent(patent, username)
        Log.e("PatentViewModel", "PatentData: $patent")
        call.buyPatent(buyPatent).enqueue(object : Callback<Boolean> {
            override fun onResponse(
                call: Call<Boolean>,
                response: Response<Boolean>
            ) {
                if (response.isSuccessful) {
                    _is_patent_accquired.value = response.body() ?: false
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
    }
}
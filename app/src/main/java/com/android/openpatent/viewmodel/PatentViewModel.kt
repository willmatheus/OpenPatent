package com.android.openpatent.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.openpatent.network.PatentData
import com.android.openpatent.network.RetrofitService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PatentViewModel : ViewModel() {

    var message: String = ""
    private val call = RetrofitService.api
    val _patents = MutableStateFlow<List<PatentData>>(emptyList())
    val patents: StateFlow<List<PatentData>> = _patents

    fun registerPatent(inventor: String, title: String, description: String, onResult: (Boolean) -> Unit) {
        val request = PatentData(inventor, title, description)

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
}
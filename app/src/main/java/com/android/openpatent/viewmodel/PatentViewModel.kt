package com.android.openpatent.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.openpatent.network.Block
import com.android.openpatent.network.MensagemResposta
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
    val _patents = MutableStateFlow<List<Block>>(emptyList())
    val patents: StateFlow<List<Block>> = _patents

    fun registerPatent(inventor: String, title: String, description: String, onResult: (Boolean) -> Unit) {
        val request = PatentData(inventor, title, description)

        call.registerPatent(request).enqueue(object : Callback<MensagemResposta> {
            override fun onResponse(
                call: Call<MensagemResposta>,
                response: Response<MensagemResposta>
            ) {
                message = (response.body() ?: "Erro desconhecido").toString()
                onResult(true)
            }

            override fun onFailure(call: Call<MensagemResposta>, t: Throwable) {
                message = "Falha: ${t.localizedMessage}"
                onResult(false)
            }
        })
    }

    fun getPatents() {
        call.getBlockChain().enqueue(object : Callback<List<Block>> {
            override fun onResponse(
                call: Call<List<Block>>,
                response: Response<List<Block>>
            ) {
                if (response.isSuccessful) {
                    _patents.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<Block>>, t: Throwable) {
                Log.e("VM", "Erro ao carregar patentes: ${t.message}")
            }
        })
    }
}
package com.android.openpatent.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class MensagemResposta(
    val mensagem: String
)

data class Block(
    val index: Int,
    val timestamp: String,
    val data: PatentData,
    val previousHash: String,
    val hash: String
)

data class PatentData(
    val title: String,
    val inventor: String,
    val description: String
)

interface IPatentNetwork {
    @POST("patentes")
    fun registerPatent(@Body patent: PatentData): Call<MensagemResposta>

    @GET("cadeia")
    fun getBlockChain(): Call<List<Block>>

    @GET("valida")
    fun validarBlockchain(): Call<MensagemResposta>
}
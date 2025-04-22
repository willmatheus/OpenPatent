package com.android.openpatent.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    val api: IPatentNetwork by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.1.14:8080") // 10.0.2.2 = localhost no emulador Android
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IPatentNetwork::class.java)
    }
}
package com.example.testapp.network

import android.app.Application
import retrofit2.Retrofit

class AppApplication : Application() {

    lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .build()
    }

}
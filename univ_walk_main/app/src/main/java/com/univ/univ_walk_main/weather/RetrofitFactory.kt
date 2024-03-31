package com.univ.univ_walk_main.weather

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun create(): RetrofitService {

        val retrofit = Retrofit.Builder() //레트로핏 객체 생성
            .baseUrl(BASE_URL) //서버URL
            .addConverterFactory(GsonConverterFactory.create()) //set converter
            .build()

        return retrofit.create(RetrofitService::class.java)
    }

}
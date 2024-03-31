package com.univ.univ_walk_main.weather

import com.univ.univ_walk_main.weather.Model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//인터페이스
interface RetrofitService {
    @GET("weather?") //get방식 url의 endpoint: API가 서버에서 특정 부분에 접근할 수 있도록 하는 역할
    fun getWeather( //url 파라미터
        @Query("lat")
        lat: String,
        @Query("lon")
        lon: String,
        @Query("appid")
        appid: String
    ): Call<WeatherModel> //get에 대한 응답데이터를 받아서 dto화 할 클래스 제네릭으로 지정
    //참고: https://jaejong.tistory.com/33
}
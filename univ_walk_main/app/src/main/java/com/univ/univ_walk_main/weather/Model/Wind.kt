package com.univ.univ_walk_main.weather.Model

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") var speed : Double,
    @SerializedName("deg") var deg : Int,
    @SerializedName("gust") var gust : Double
)

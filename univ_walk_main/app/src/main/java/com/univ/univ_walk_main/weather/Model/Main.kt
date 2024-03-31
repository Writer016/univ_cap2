package com.univ.univ_walk_main.weather.Model

import com.google.gson.annotations.SerializedName

data class Main(
    @SerializedName("temp") var temp : Double,
    @SerializedName("feels_like") var feels_like : Double,
    @SerializedName("temp_min") var temp_min : Double,
    @SerializedName("temp_max") var temp_max : Double,
    @SerializedName("pressure") var pressure : Int,
    @SerializedName("humidity") var humidity : Int,
    @SerializedName("sea_level") var sea_level : Int,
    @SerializedName("grnd_level") var grnd_level : Int
)

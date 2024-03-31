package com.univ.univ_walk_main.weather.Model

import com.google.gson.annotations.SerializedName

data class Coord (
    @SerializedName("lon") var lon: Float,
    @SerializedName("lat") var lat: Float
    )
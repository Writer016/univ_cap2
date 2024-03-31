package com.univ.univ_walk_main.weather.Model

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var all: Int
)

package com.univ.univ_walk_main.weather.Model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("type") var type : Int,
    @SerializedName("id") var id : Int,
    @SerializedName("country") var country : String,
    @SerializedName("sunrise") var sunrise : Int,
    @SerializedName("sunset") var sunset : Int
)

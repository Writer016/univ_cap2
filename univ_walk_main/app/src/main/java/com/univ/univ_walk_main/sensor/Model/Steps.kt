package com.example.kotlin_mvvm.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stepsDB")
data class Steps(
    var year: Int,
    var month: Int,
    var date: Int,
    var steps: Int,
    @PrimaryKey(autoGenerate=true) var ID: Long = 0
)
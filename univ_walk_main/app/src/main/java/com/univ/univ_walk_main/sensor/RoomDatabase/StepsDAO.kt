package com.example.kotlin_mvvm.RoomDatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kotlin_mvvm.Model.Steps

@Dao
interface StepsDAO { //Database 접근 위한 인터페이스
    @Insert
    fun insertSteps(steps: Steps) //insert문

    @Update
    fun updateSteps(steps: Steps) //update문

    @Delete
    fun deleteSteps(steps: Steps)

    @Query("SELECT * FROM stepsDB")
    fun getAll(): List<Steps>

    @Query("SELECT * FROM stepsDB WHERE year = :year AND month = :month AND date = :date")
    fun getTodaySteps(year: Int, month: Int, date: Int): List<Steps>
}
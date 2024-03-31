package com.example.kotlin_mvvm.RoomDatabase

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlin_mvvm.Model.Steps

//데이터베이스 관리 위한 클래스
@Database(entities = [Steps::class], version = 1) //사용되는 엔티티는 Steps
abstract class StepsDatabase: RoomDatabase() {
    abstract fun stepsDAO(): StepsDAO
}
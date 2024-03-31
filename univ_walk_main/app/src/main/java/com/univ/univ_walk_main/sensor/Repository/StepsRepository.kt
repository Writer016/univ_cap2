package com.example.kotlin_mvvm.Repository

import android.app.Application
import androidx.room.Room
import com.example.kotlin_mvvm.RoomDatabase.StepsDAO
import com.example.kotlin_mvvm.RoomDatabase.StepsDatabase
import com.example.kotlin_mvvm.Model.Steps

class StepsRepository(application: Application) {
    val db = Room.databaseBuilder(application, StepsDatabase::class.java, "stepsDB").build() //DB 관리 위한 클래스의 인스턴스 생성
    val dao: StepsDAO = db.stepsDAO() //Database 접근 위한 인터페이스 가져오기

    fun insert(steps: Steps){
        dao.insertSteps(steps)
    }

    fun update(steps: Steps){
        dao.updateSteps(steps)
    }

    fun delete(steps: Steps){
        dao.deleteSteps(steps)
    }

    fun getAll(): List<Steps>{
        return dao.getAll()
    }

    fun getTodaySteps(year: Int, month: Int, date: Int): List<Steps>{
        return dao.getTodaySteps(year, month, date)
    }
}
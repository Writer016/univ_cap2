package com.example.kotlin_mvvm.ViewModel
import android.util.Log
import androidx.lifecycle.*
import com.example.kotlin_mvvm.Model.Steps
import com.example.kotlin_mvvm.Repository.StepsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StepsViewModel(private val repo: StepsRepository): ViewModel() {
    val dis = Dispatchers.IO

    fun insert(steps: Steps){
        viewModelScope.launch(dis) {
            repo.insert(steps)
        }
    }

    fun update(steps: Steps){
        viewModelScope.launch(dis) {
            repo.update(steps)
        }
    }

    fun delete(steps: Steps){
        viewModelScope.launch(dis) {
            repo.delete(steps)
        }
    }

    //모든 알람 목록 가져오기
    fun getAllSteps(): List<Steps>{
        return repo.getAll()
    }

    fun getTodaySteps(year: Int, month: Int, date: Int): List<Steps>{
        return repo.getTodaySteps(year, month, date)
    }

}
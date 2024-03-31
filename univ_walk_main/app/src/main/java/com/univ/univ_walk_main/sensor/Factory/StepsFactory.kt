package com.univ.univ_walk_main.sensor.Factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_mvvm.Repository.StepsRepository
import com.example.kotlin_mvvm.ViewModel.StepsViewModel

class StepsFactory(private val repo: StepsRepository): ViewModelProvider.Factory { //커스텀 팩토리
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StepsViewModel(repo) as T
    }
}
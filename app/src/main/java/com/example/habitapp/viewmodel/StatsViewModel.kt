package com.example.habitapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class StatsData(val completedTasks: Int, val habitsDone: Int, val streak: Int, val totalProgressPercent: Int)

class StatsViewModel : ViewModel() {
    private val _stats = MutableLiveData(StatsData(12, 28, 7, 64))
    val stats: LiveData<StatsData> = _stats

    fun refresh() {
       
    }
}


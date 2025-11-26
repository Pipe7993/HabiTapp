package com.example.habitapp.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is home Fragment")
    val text: StateFlow<String> = _text
}
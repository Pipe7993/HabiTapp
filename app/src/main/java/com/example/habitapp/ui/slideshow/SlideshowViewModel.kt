package com.example.habitapp.ui.slideshow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SlideshowViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is slideshow Fragment")
    val text: StateFlow<String> = _text
}
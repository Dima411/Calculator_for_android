package com.example.mypetprojectalculator.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    val enteredValues = mutableStateOf(listOf("0"))
}
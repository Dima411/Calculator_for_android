package com.example.mypetprojectalculator.viewmodel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mypetprojectalculator.ui.theme.MyPetProjectСalculatorTheme
import com.example.mypetprojectalculator.view.CalculatorActivity
import com.example.mypetprojectalculator.model.SettingsManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val settingsManager = SettingsManager(this)
        settingsManager.applyLanguage()

        setContent {
            MyPetProjectСalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorActivity()
                }
            }
        }
    }
}

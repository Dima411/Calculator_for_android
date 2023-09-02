package com.example.mypetprojectalculator.view

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.example.mypetprojectalculator.model.SettingsManager
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun StatusBarColor(context: Context) {
    val settingsManager = SettingsManager(context)
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = if (settingsManager.currentTheme.value == "light") {
        !isSystemInDarkTheme()
    } else isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = settingsManager.colorBackground(),
            darkIcons = useDarkIcons
        )
    }
}
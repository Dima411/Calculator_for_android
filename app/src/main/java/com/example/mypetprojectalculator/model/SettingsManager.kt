package com.example.mypetprojectalculator.model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.mypetprojectalculator.R
import com.example.mypetprojectalculator.ui.theme.ColorLightNumberOnButton
import com.example.mypetprojectalculator.ui.theme.ColorNumberOnButton
import com.example.mypetprojectalculator.ui.theme.ColorPressableButtonWhite
import com.example.mypetprojectalculator.ui.theme.ColorPressableButtonYellow
import java.util.Locale

class SettingsManager(private val context: Context) {
    private val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    val currentTheme = mutableStateOf(sharedPref.getString("theme", "light") ?: "light")
    val currentLanguage = mutableStateOf(sharedPref.getString("language", Locale.getDefault()
        .language) ?: "uk")

    fun saveTheme(theme: String) {
        val editor = sharedPref.edit()
        editor.putString("theme", theme)
        editor.apply()
        currentTheme.value = theme
    }

    fun saveLanguage(language: String) {
        val editor = sharedPref.edit()
        editor.putString("language", language)
        editor.apply()
        currentLanguage.value = language
    }

    fun colorContentNotNumber() : Color {
        return if (currentTheme.value == "dark") Color.White else Color.Black
    }

    fun colorContentNumber() : Color {
        return if (currentTheme.value == "dark") ColorLightNumberOnButton else ColorNumberOnButton
    }

    fun colorBackground() : Color {
        return if (currentTheme.value == "dark") Color.Black else Color.White
    }

    fun colorPressableButton() : Color {
        return if (currentTheme.value == "dark")
            ColorPressableButtonWhite else ColorPressableButtonYellow
    }

    fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun applyLanguage() {
        val local = Locale(currentLanguage.value)
        setLocale(local)
    }

    fun translatedText(stringResId: Int) : String {
        return when (currentLanguage.value) {
                "uk" -> context.getString(stringResId)
                else -> context.getString(stringResId)
            }
    }

}

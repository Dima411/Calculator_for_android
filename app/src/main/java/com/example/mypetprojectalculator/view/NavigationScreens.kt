package com.example.mypetprojectalculator.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mypetprojectalculator.viewmodel.CalculatorViewModel

@Composable
fun NavigationScreens() {
    val navController = rememberNavController()
    val sharedViewModel = viewModel<CalculatorViewModel>()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "screen_1") {
        composable("screen_1") {
            CalculatorFragment(context, sharedViewModel, onButtonClick = { navController.navigate("screen_2") })
        }
        composable("screen_2") {
            SettingsFragment(context, sharedViewModel, onButtonClick = { navController.navigate("screen_1") })
        }
    }
}
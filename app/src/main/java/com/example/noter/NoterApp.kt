package com.example.noter

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.noter.ui.navigation.NoterNavHost

@Composable
fun NoterApp(
    navController: NavHostController = rememberNavController(),
    switchMode: () -> Unit
){
    NoterNavHost(navController = navController, switchMode)
}
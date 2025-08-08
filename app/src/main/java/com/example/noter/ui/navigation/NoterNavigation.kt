package com.example.noter.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.noter.ui.HomeScreen
import com.example.noter.ui.HomeScreenDestination
import com.example.noter.ui.note.TodoNoteScreen
import com.example.noter.ui.note.TodoNoteScreenDestination

@Composable
fun NoterNavHost(
    navController: NavHostController,
    switchMode: () -> Unit,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = HomeScreenDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                navigateToNote = { navController.navigate("${TodoNoteScreenDestination.route}/${it}") },
                switchMode
            )
        }
        composable(
            route = TodoNoteScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(TodoNoteScreenDestination.NOTE_ID) {
                type = NavType.IntType
            })
        ) {
            TodoNoteScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
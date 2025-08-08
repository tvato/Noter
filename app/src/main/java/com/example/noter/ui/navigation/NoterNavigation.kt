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
import com.example.noter.ui.note.NoteScreen
import com.example.noter.ui.note.NoteScreenDestination

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
                navigateToNote = { navController.navigate("${NoteScreenDestination.route}/${it}") },
                switchMode
            )
        }
        composable(
            route = NoteScreenDestination.routeWithArgs,
            arguments = listOf(navArgument(NoteScreenDestination.NOTE_ID) {
                type = NavType.IntType
            })
        ) {
            NoteScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
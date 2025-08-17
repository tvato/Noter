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
import com.example.noter.ui.note.TextNoteScreen
import com.example.noter.ui.note.TodoNoteScreen

object NoteScreenDestination: NavigationDestination{
    override val route = "note"
    const val NOTE_ID = "noteId"
    const val TYPE = "type"
    val routeWithArgs = "$route/{$TYPE}/{$NOTE_ID}"
}

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
        // No idea what I was doing here... but it works...
        composable(route = HomeScreenDestination.route) {
            HomeScreen(
                navigateToNote = { navController.navigate(route = "note/$it") },
                switchMode
            )
        }
        composable(
            route = "note/1/{noteId}",
            arguments = listOf(
                navArgument(NoteScreenDestination.NOTE_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            TodoNoteScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = "note/0/{noteId}",
            arguments = listOf(
                navArgument(NoteScreenDestination.NOTE_ID){
                    type = NavType.IntType
                }
            )
        ){
            TextNoteScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
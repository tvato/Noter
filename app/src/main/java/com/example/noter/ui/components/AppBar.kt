package com.example.noter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.noter.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(                                     // Used in HomeScreen.kt
    switchMode: () -> Unit
){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(1f).padding(end = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.align(Alignment.Center).padding(end = 15.dp)
                )
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.description_theme_switch),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 10.dp)
                        .clickable { switchMode() }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(                                     // Used in TodoNoteScreen.kt
    navigateBack: () -> Unit,
    canNavigateBack: Boolean,
    deleteNote: () -> Unit,
    saveNote: () -> Unit
){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth(1f).padding(end = 5.dp)
            ) {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.align(Alignment.Center).padding(end = 40.dp)
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.description_delete_note),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable {
                            deleteNote()
                            navigateBack()
                        }
                )
            }
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = {
                    saveNote()
                    navigateBack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        }
    )
}
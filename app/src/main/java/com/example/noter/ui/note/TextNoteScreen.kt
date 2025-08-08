package com.example.noter.ui.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noter.ui.AppViewModelProvider
import com.example.noter.ui.components.AppBar
import com.example.noter.ui.navigation.NavigationDestination
import com.example.noter.ui.theme.NoterTheme

object TextNoteScreenDestination: NavigationDestination{
    override val route = "text_note"
    const val NOTE_ID = "noteId"
    val routeWithArgs = "$route/{$NOTE_ID}"
}

@Composable
fun TextNoteScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.noteState

    Scaffold(
        topBar = {
            AppBar(
                navigateBack = navigateBack,
                canNavigateBack = canNavigateBack,
                deleteNote = viewModel::deleteNote,
                saveNote = viewModel::saveNote
            )
        },
        modifier = Modifier.imePadding()
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            BasicTextField(
                value = uiState.note.title,
                onValueChange = { },
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {}
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 25.dp, bottom = 10.dp, top = 15.dp)
            )
            Column() {
                TextField(
                    value = uiState.contents.toString(),
                    onValueChange = {},
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun TextNotePreview(){
    NoterTheme {
        Scaffold(
            topBar = {
                AppBar(
                    navigateBack = {},
                    canNavigateBack = true,
                    deleteNote = {},
                    saveNote = {}
                )
            },
            modifier = Modifier.imePadding()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                BasicTextField(
                    value = "Title",
                    onValueChange = { },
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {}
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, bottom = 10.dp, top = 15.dp)
                )
                Column() {
                    TextField(
                        value = "Content here",
                        onValueChange = {},
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}
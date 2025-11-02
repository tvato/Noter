package com.example.noter.ui.note

import android.view.ViewTreeObserver
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noter.ui.AppViewModelProvider
import com.example.noter.ui.components.AppBar

@Composable
fun TextNoteScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.noteState

    // Used in DisposableEffect only
    val rootView = LocalView.current
    val viewTree = rootView.viewTreeObserver
    val manager = LocalFocusManager.current

    // Clear focus of TextField when keyboard is closed
    DisposableEffect(viewTree){
        val listener = ViewTreeObserver.OnGlobalLayoutListener{
            val isVisible = ViewCompat.getRootWindowInsets(rootView)?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            if(!isVisible){
                manager.clearFocus()
            }
        }
        viewTree.addOnGlobalLayoutListener(listener)
        onDispose{ viewTree.removeOnGlobalLayoutListener(listener) }
    }

    BackHandler{
        viewModel.addText(uiState.contents[0])
        viewModel.saveTextNote()
        navigateBack()
    }

    Scaffold(
        topBar = {
            AppBar(
                navigateBack = navigateBack,
                canNavigateBack = canNavigateBack,
                viewModel = viewModel
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
                onValueChange = { viewModel.updateTitleState(it) },
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
                    value = if(uiState.contents.isNotEmpty()) uiState.contents[0].text else "",
                    onValueChange = { viewModel.updateTextState(it) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
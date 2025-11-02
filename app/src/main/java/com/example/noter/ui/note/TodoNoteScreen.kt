package com.example.noter.ui.note

import android.view.ViewTreeObserver
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noter.R
import com.example.noter.data.Content
import com.example.noter.ui.AppViewModelProvider
import com.example.noter.ui.components.AppBar

@Composable
fun TodoNoteScreen(
    navigateBack: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: NoteViewModel = viewModel(factory = AppViewModelProvider.Factory),
){

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
                viewModel.saveNote()
            }
        }
        viewTree.addOnGlobalLayoutListener(listener)
        onDispose{ viewTree.removeOnGlobalLayoutListener(listener) }
    }

    BackHandler{
        viewModel.saveNote()
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
        NoteBody(
            innerPadding = innerPadding,
            viewModel = viewModel
        )
    }
}

@Composable
fun NoteBody(
    innerPadding: PaddingValues,
    viewModel: NoteViewModel
){
    val newItem = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ){
        NoteTitle(
            viewModel = viewModel,
            newItem = newItem
        )
        LazyColumn(modifier = Modifier.padding(0.dp)){
            itemsIndexed(viewModel.noteState.contents){ index, content ->
                if(!content.checked)
                    NoteContentRow(
                        viewModel = viewModel,
                        content = content,
                        newItem = newItem,
                        contentsSize = viewModel.noteState.contents.size,
                        previousContent = if(index != 0) viewModel.noteState.contents[index-1] else Content(0,0,"",false, 0, 1, 0),
                    )
            }
            item{
                AddItem(
                    viewModel = viewModel,
                    newItem = newItem
                )
            }
            itemsIndexed(viewModel.noteState.contents){ index, content ->
                if(content.checked || (viewModel.isParent(content.id) && viewModel.checkChecked(content.id) > 0)){
                    CheckedItems(
                        viewModel = viewModel,
                        content = content,
                    )
                }
            }
        }
    }
}

@Composable
fun NoteTitle(
    viewModel: NoteViewModel,
    newItem: MutableState<Boolean>
){
    BasicTextField(
        value = viewModel.noteState.note.title,
        onValueChange = { viewModel.updateTitleState(it) },
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = {
                viewModel.saveNote()
                viewModel.addItem(0, -1, 0)
                newItem.value = true
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp, bottom = 10.dp, top = 15.dp)
    )
}

@Composable
fun NoteContentRow(
    viewModel: NoteViewModel,
    content: Content,
    newItem: MutableState<Boolean>,
    contentsSize: Int,
    previousContent: Content,
){
    var isFocused by remember { mutableStateOf(false) }
    var offset = content.offset
    val newItemFocus = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val toggleFull = viewModel.checkChecked(content.id)
    val triState = when(toggleFull) {
        0 -> ToggleableState.Off
        1 -> ToggleableState.Indeterminate
        2 -> ToggleableState.On
        else -> ToggleableState.Off
    }

    // Transfers focus to new TextField
    LaunchedEffect(newItem){
        if(newItem.value){
            newItemFocus.requestFocus()
            newItem.value = false
        }
    }

    Row(
        modifier = Modifier
            .height(40.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .offset { IntOffset(offset.toInt(), 0) }
    ){
        if(isFocused) {
            if(offset > 0) Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                contentDescription = stringResource(R.string.description_unindent),
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
                    .offset(x = 5.dp)
                    .clickable {
                        offset -= 100
                        viewModel.updateContent(
                            content.copy(
                                offset = offset,
                                parent = viewModel.findParent(content, offset)?.id ?: 0 //if (offset > 0) content.parent else 0
                            ),
                            content.id
                        )
                    }
            )
            if(offset < 200) Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_right),
                contentDescription = stringResource(R.string.description_indent),
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
                    .offset(x = 10.dp)
                    .clickable {
                        if (previousContent.offset == 0) {
                            offset = 100
                            viewModel.updateContent(
                                content.copy(parent = previousContent.id, offset = offset),
                                content.id
                            )
                        }else{
                            offset += 100
                            viewModel.updateContent(
                                content.copy(
                                    offset = offset,
                                    parent = viewModel.findParent(content, offset)?.id ?: 1
                                ),
                                content.id
                            )
                        }
                    }
            )
        }
        if(viewModel.isParent(content.id)){
            TriStateCheckbox(
                state = triState,
                onClick = {
                    viewModel.checkAllAndUpdate(content.id)
                },
                modifier = Modifier.align(Alignment.CenterVertically),
                colors = CheckboxDefaults.colors().copy(
                    // Still could be better, especially for light theme
                    // But I'll leave them like this, for now...
                    checkedBoxColor = MaterialTheme.colorScheme.inversePrimary,
                    checkedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )
        }else{
            Checkbox(
                checked = content.checked,
                onCheckedChange = {
                    viewModel.updateContent(
                        content.copy(checked = it),
                        content.id
                    )
                },
                colors = CheckboxDefaults.colors().copy(
                    // Still could be better, especially for light theme
                    // But I'll leave them like this, for now...
                    checkedBoxColor = MaterialTheme.colorScheme.inversePrimary,
                    checkedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,

                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        BasicTextField(
            value = content.text,
            onValueChange = {
                viewModel.updateContent(
                    content.copy(text = it),
                    content.id)
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    viewModel.saveNote()
                    viewModel.addItem(offset, content.line + 1, viewModel.findParent(content, offset)?.id ?: 0)
                    if(content.line == contentsSize) newItem.value = true
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            modifier =  Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
                .focusRequester(newItemFocus)
        )
        if(isFocused) Icon(
                imageVector = ImageVector.vectorResource(R.drawable.delete),
                contentDescription = stringResource(R.string.description_delete_item),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .offset { if (offset != 0) IntOffset(-offset, 0) else IntOffset(0, 0) }
                    .clickable(onClick = { viewModel.deleteContent(content) })
        )
    }
}


@Composable
fun AddItem(
    viewModel: NoteViewModel,
    newItem: MutableState<Boolean>
){
    Surface(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(start = 20.dp, top = 10.dp),
        onClick = {
            viewModel.saveNote()
            viewModel.addItem(0, -1, 0)
            newItem.value = true
        },
        color = MaterialTheme.colorScheme.secondaryContainer
    ){
        Row{
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.plus),
                contentDescription = stringResource(R.string.description_add_item),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "Add item",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp)
            )
        }
    }
}

@Composable
fun CheckedItems(
    viewModel: NoteViewModel,
    content: Content,
){
    val toggleFull = viewModel.checkChecked(content.id)
    val triState = when(toggleFull) {
        0 -> ToggleableState.Off
        1 -> ToggleableState.Indeterminate
        2 -> ToggleableState.On
        else -> ToggleableState.Off
    }
    Row(
        modifier = Modifier
            .height(40.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .offset { IntOffset(content.offset, 0) }
    ){
        if(viewModel.isParent(content.id)) {
            TriStateCheckbox(
                state = triState,
                onClick = {
                    viewModel.uncheckAllAndUpdate(content.id)
                },
                colors = CheckboxDefaults.colors().copy(
                    // Still could be better, especially for light theme
                    // But I'll leave them like this, for now...
                    checkedBoxColor = MaterialTheme.colorScheme.inversePrimary,
                    checkedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }else{
            Checkbox(
                checked = content.checked,
                onCheckedChange = {
                    if(content.parent != 0){
                        val parentContent = viewModel.findParent(content, content.offset) ?: Content(
                            0,0,"This should have not happened... I'm sorry...",false,0,0,0
                        )
                        viewModel.updateContent(
                            parentContent.copy(checked = false),
                            content.parent
                        )
                    }
                    viewModel.updateContent(
                        content.copy(checked = it),
                        content.id
                    )
                },
                colors = CheckboxDefaults.colors().copy(
                    // Still could be better, especially for light theme
                    // But I'll leave them like this, for now...
                    checkedBoxColor = MaterialTheme.colorScheme.inversePrimary,
                    checkedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        BasicTextField(
            value = content.text,
            onValueChange = {
                viewModel.updateContent(
                    content.copy(text = it),
                    content.id)
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textDecoration = TextDecoration.LineThrough
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier =  Modifier
                .align(Alignment.CenterVertically)
                .weight(1f)
        )
    }
}

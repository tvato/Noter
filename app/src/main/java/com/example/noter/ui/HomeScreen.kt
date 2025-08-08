package com.example.noter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noter.R
import com.example.noter.data.Content
import com.example.noter.data.Note
import com.example.noter.data.NoteAndContent
import com.example.noter.ui.components.AppBar
import com.example.noter.ui.navigation.NavigationDestination
import com.example.noter.ui.theme.NoterTheme

object HomeScreenDestination: NavigationDestination {
    override val route = "home"
}

@Composable
fun HomeScreen(
    navigateToNote: (Int) -> Unit,
    switchMode: () -> Unit,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.uiState.collectAsState()

    HomeScreenContainer(
        navigateToNote = navigateToNote,
        switchMode = switchMode,
        addNote = viewModel::addNote,
        uiState = uiState
    )
}

@Composable
fun HomeScreenContainer(
    navigateToNote: (Int) -> Unit,
    switchMode: () -> Unit,
    addNote: () -> Unit,
    uiState: State<NoteUiState>
){
    Scaffold(
        topBar = {
            AppBar(switchMode)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addNote()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.description_add_note)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) { innerPadding ->
        NoteList(
            uiState = uiState,
            navigateToNote = navigateToNote,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun NoteList(
    uiState: State<NoteUiState>,
    navigateToNote: (Int) -> Unit,
    innerPadding: PaddingValues
){
    Column(
        modifier = Modifier.padding(innerPadding)
    ){
        LazyColumn(
            modifier = Modifier.padding(5.dp)
        ) {
            //items(uiState.value.notesWithContent) { noteAndContent ->
            items(uiState.value.notesWithContent) { noteAndContent ->
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { navigateToNote(noteAndContent.note.id) }
                ) {
                    NoteBody(
                        noteAndContent = noteAndContent
                    )
                }
            }
        }
    }
}

@Composable
fun NoteBody(
    noteAndContent: NoteAndContent,
){
    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 5.dp)
    ){
        Column(modifier = Modifier.padding(10.dp)){
            Text(
                text = noteAndContent.note.title,
                modifier = Modifier.padding(start = 15.dp, bottom = 5.dp),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Column(
                modifier = Modifier.padding(0.dp)
            ){
                noteAndContent.contents.forEach { content ->
                    ContentRow(
                        content = content,
                    )
                }
            }
        }
    }
}

@Composable
fun ContentRow(
    content: Content
){
    Row(
        modifier = Modifier
            .height(40.dp)
            .offset{
                IntOffset(content.offset, 0)
            }
    ){
        Checkbox(
            checked = content.checked,
            onCheckedChange = {},
            modifier = Modifier.align(Alignment.CenterVertically),
            enabled = false
        )
        Text(
            text = content.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@PreviewLightDark
@Composable
fun HomeScreenPreview(){
    val noteList: MutableList<NoteAndContent> = mutableListOf(
        NoteAndContent(
            Note(1, "Preview title 1"),
            listOf(
                Content(1, 1, "Preview text 1", true, 0, 0),
                Content(2, 1, "Preview text 2", false, 0, 1)
            )
        ),
        NoteAndContent(
            Note(1, "Preview title 2"),
            listOf(
                Content(3, 1, "Preview text 3", false, 0, 0),
                Content(4, 1, "Preview text 4", true, 0, 1),
                Content(5, 1, "Preview text 5", false, 0, 2),
                Content(6, 1, "Preview text 6", true, 0, 3)
            )
        )
    )

    NoterTheme {
        HomeScreenContainer(
            navigateToNote = {},
            switchMode = {},
            addNote = {},
            uiState = remember{mutableStateOf(NoteUiState(noteList))}
        )
    }
}
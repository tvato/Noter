package com.example.noter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
    navigateToNote: (String) -> Unit,
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
    navigateToNote: (String) -> Unit,
    switchMode: () -> Unit,
    addNote: (Byte) -> Unit,
    uiState: State<NoteUiState>
){
    Scaffold(
        topBar = {
            AppBar(switchMode)
        },
        floatingActionButton = {
            CustomFloater(
                addNote = addNote
            )
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
fun CustomFloater(
    addNote: (Byte) -> Unit
){
    val types = listOf(
        R.drawable.text,
       R.drawable.checkbox
    )
    val noteMenuVisible = remember { mutableStateOf(false) }
    Column{
        if(noteMenuVisible.value) {
            types.forEach{ type ->
                FloaterItem(
                    image = type,
                    addNote = addNote,
                    noteMenuVisible = noteMenuVisible
                )
            }
        }else{
            FloatingActionButton(
                onClick = {
                    noteMenuVisible.value = !noteMenuVisible.value
                },
                elevation = FloatingActionButtonDefaults.elevation(
                    focusedElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    pressedElevation = 0.dp,
                    defaultElevation = 0.dp
                )
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                    contentDescription = stringResource(R.string.description_add_note),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    }
}

@Composable
fun FloaterItem(
    image: Int,
    addNote: (Byte) -> Unit,
    noteMenuVisible: MutableState<Boolean>
){
    FloatingActionButton(
        // TODO: Navigate after addNote...
        //       Also, change icons
        onClick = {
            if(image == R.drawable.text){
                addNote(0)
            }else{
                addNote(1)
            }
            noteMenuVisible.value = !noteMenuVisible.value
        },
        modifier = Modifier
            .padding(top = 5.dp)
    ){
        Icon(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier.size(45.dp)
        )
    }
}

@Composable
fun NoteList(
    uiState: State<NoteUiState>,
    navigateToNote: (String) -> Unit,
    innerPadding: PaddingValues
){
    Column(
        modifier = Modifier.padding(innerPadding)
    ){
        LazyColumn(
            modifier = Modifier.padding(5.dp)
        ) {
            items(uiState.value.notesWithContent) { noteAndContent ->
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = {
                        navigateToNote("${noteAndContent.note.type}/${noteAndContent.note.id}")
                    }
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
                    if(noteAndContent.note.type.toInt() == 1) {
                        ContentRow(
                            content = content,
                        )
                    }else{
                        TextContentRow(
                            content = content
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextContentRow(
    content: Content
){
    Row(
        modifier = Modifier
            .heightIn(max = 120.dp, min = 40.dp)
            .padding(start = 15.dp)
    ){
        Text(
            text = content.text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically),
        )
    }
}

@Composable
fun ContentRow(
    content: Content
){
    Row(
        modifier = Modifier
            .height(30.dp)
            .offset {
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
            Note(1, "Preview title 1", 0),
            listOf(
                Content(1, 1, "Long preview text to show something.\nHere some new lines.\nAnd stuff.\nAnd more stuff.\nAnd some more.", false, 0, 0)
            )
        ),
        NoteAndContent(
            Note(1, "Preview title 2", 1),
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
            uiState = remember { mutableStateOf(NoteUiState(noteList)) }
        )
    }
}
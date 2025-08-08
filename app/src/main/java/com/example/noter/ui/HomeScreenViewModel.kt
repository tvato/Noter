package com.example.noter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.data.Note
import com.example.noter.data.NoteAndContent
import com.example.noter.data.NotesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NoteUiState(
    val notesWithContent: List<NoteAndContent> = listOf()
)

class HomeScreenViewModel(
    private val notesRepository: NotesRepository
): ViewModel() {

    val uiState: StateFlow<NoteUiState> = notesRepository.getAllNotesWithContent()
        .map{
            NoteUiState(changeType(it))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = NoteUiState()
        )

    fun addNote(){
        val newNote = Note(
            title = "New note",
            type = 1
        )

        viewModelScope.launch {
            notesRepository.insertNote(newNote)
        }
    }
}

fun changeType(noteAndContents: List<NoteAndContent>): MutableList<NoteAndContent> {
    val listOfUiState: MutableList<NoteAndContent> = mutableListOf()
    noteAndContents.forEach { noteAndContent ->
        listOfUiState.add(NoteAndContent(
            note = noteAndContent.note,
            contents = noteAndContent.contents.sortedBy { it.line }
        ))
    }

    return listOfUiState
}
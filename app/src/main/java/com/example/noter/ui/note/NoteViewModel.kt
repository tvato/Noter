package com.example.noter.ui.note

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noter.data.Content
import com.example.noter.data.Note
import com.example.noter.data.NoteAndContent
import com.example.noter.data.NotesRepository
import com.example.noter.ui.navigation.NoteScreenDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class NoteState(
    val note: Note = Note(0, "", 1),
    val contents: List<Content> = listOf()
)

class NoteViewModel(
    savedStateHandle: SavedStateHandle,
    private val notesRepository: NotesRepository
): ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle[NoteScreenDestination.NOTE_ID])

    var noteState by mutableStateOf(NoteState())

    init{
        viewModelScope.launch{
            noteState = notesRepository.getNoteWithContent(noteId)
                .filterNotNull()
                .first()
                .toNoteState()
        }
    }

    fun updateContent(newContent: Content, id: Int){
        val newContents: MutableList<Content> = mutableListOf()
        noteState.contents.forEach{ content ->
            if(id == content.id){
                newContents.add(newContent)
            }else{
                newContents.add(content)
            }
        }
        noteState = NoteState(
            note = noteState.note,
            contents = newContents
        )
    }

    fun updateLineNum(line: Int){
        val newContents: MutableList<Content> = mutableListOf()
        noteState.contents.forEach{ content ->
            if(content.line >= line){
                newContents.add(
                    Content(
                        id = content.id,
                        noteId = content.noteId,
                        text = content.text,
                        checked = content.checked,
                        offset = content.offset,
                        line = content.line + 1,
                        group = content.group
                    )
                )
            }else{
                newContents.add(content)
            }
        }

        noteState = NoteState(
            note = noteState.note,
            contents = newContents
        )

        viewModelScope.launch {
            notesRepository.updateContents(newContents)
        }
    }

    fun addItem(offset: Int, line: Int, group: Int){
        if(line != -1){
            updateLineNum(line)
        }

        val newLine = if(noteState.contents.isEmpty()) 1 else if(line == -1) noteState.contents.last().line + 1 else line

        val newContent = Content(
            noteId = noteId,
            text = "",
            checked = false,
            offset = offset,
            line = newLine,
            group = group
        )

        viewModelScope.launch{
            notesRepository.insertContent(newContent)
            noteState = notesRepository.getNoteWithContent(noteId)
                .filterNotNull()
                .first()
                .toNoteState()
        }
    }

    fun addText(content: Content){
        viewModelScope.launch{
            notesRepository.insertContent(content)
        }
    }

    fun deleteNote(){
        viewModelScope.launch{
            notesRepository.deleteNote(noteState.note)
        }

        noteState = NoteState()
    }

    fun deleteContent(content: Content){
        val newContents: MutableList<Content> = noteState.contents.toMutableList()

        newContents.remove(content)

        noteState = NoteState(
            note = noteState.note,
            contents = newContents
        )

        viewModelScope.launch{
            notesRepository.deleteContent(content)
        }
    }

    fun updateTitleState(newTitle: String){
        noteState = NoteState(
            note = noteState.note.copy(title = newTitle),
            contents = noteState.contents
        )
    }

    fun updateTextState(newText: String){
        noteState = NoteState(
            note = noteState.note,
            contents = listOf(
                if(noteState.contents.isEmpty()){
                    Content(
                        noteId = noteState.note.id,
                        text = newText,
                        checked = false,
                        offset = 0,
                        line = 1,
                        group = 0
                    )
                }else{
                    noteState.contents[0].copy(text = newText)
                }
            )
        )
    }

    fun saveTextNote(){
        viewModelScope.launch {
            notesRepository.updateNote(noteState.note)
            notesRepository.updateContent(noteState.contents[0])
        }
    }
    fun saveNote(){
        viewModelScope.launch {
            notesRepository.updateNote(noteState.note)
            notesRepository.updateContents(noteState.contents)
        }
    }
}

fun NoteAndContent.toNoteState(): NoteState = NoteState(
    note = this.note,
    contents = if(this.contents.isEmpty() && this.note.type.toInt() == 0) listOf(
        Content(
            noteId = this.note.id,
            text = "",
            checked = false,
            offset = 0,
            line = 1,
            group = 0
        )
    ) else this.contents.sortedBy { it.line }
)
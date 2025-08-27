package com.example.noter.data

import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotesWithContent(): Flow<List<NoteAndContent>>
    fun getNoteWithContent(id: Int): Flow<NoteAndContent>

    fun getAllNotesStream(): Flow<List<Note>>
    fun getNoteStream(id: Int): Flow<Note>

    fun getAllContentsStream(): Flow<List<Content>>
    fun getContentStream(id: Int): Flow<Content>

    suspend fun insertNote(note: Note): Long
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)

    suspend fun insertContent(content: Content)
    suspend fun deleteContent(content: Content)
    suspend fun updateContent(content: Content)

    suspend fun insertContents(contents: List<Content>)
    suspend fun deleteContents(contents: List<Content>)
    suspend fun updateContents(contents: List<Content>)
}
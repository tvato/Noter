package com.example.noter.data

import kotlinx.coroutines.flow.Flow

class OfflineNotesRepository(private val noteDao: NoteDao): NotesRepository {
    override fun getAllNotesWithContent(): Flow<List<NoteAndContent>> = noteDao.getAllNotesWithContent()
    override fun getNoteWithContent(id: Int): Flow<NoteAndContent> = noteDao.getNoteWithContent(id)

    override fun getAllNotesStream(): Flow<List<Note>> = noteDao.getAllNotes()
    override fun getNoteStream(id: Int): Flow<Note> = noteDao.getNote(id)

    override fun getAllContentsStream(): Flow<List<Content>> = noteDao.getAllContents()
    override fun getContentStream(id: Int): Flow<Content> = noteDao.getContent(id)

    //override suspend fun insertNoteAndContent(noteAndContent: NoteAndContent) = noteDao.insertNoteAndContent(noteAndContent)
    //override suspend fun deleteNoteAndContent(noteAndContent: NoteAndContent) = noteDao.deleteNoteAndContent(noteAndContent)
    //override suspend fun updateNoteAndContent(noteAndContent: NoteAndContent) = noteDao.updateNoteAndContent(noteAndContent)

    override suspend fun insertNote(note: Note): Long = noteDao.insertNote(note)
    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun insertContent(content: Content) = noteDao.insertContent(content)
    override suspend fun deleteContent(content: Content) = noteDao.deleteContent(content)
    override suspend fun updateContent(content: Content) = noteDao.updateContent(content)

    override suspend fun insertContents(contents: List<Content>) = noteDao.insertContents(contents)
    override suspend fun deleteContents(contents: List<Content>) = noteDao.deleteContents(contents)
    override suspend fun updateContents(contents: List<Content>) = noteDao.updateContents(contents)
}
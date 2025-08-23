package com.example.noter.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    // Get notes and contents
    @Transaction
    @Query("SELECT * FROM notes")
    fun getAllNotesWithContent(): Flow<List<NoteAndContent>>

    @Transaction
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteWithContent(id: Int): Flow<NoteAndContent>

    // Get notes
    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id: Int): Flow<Note>

    // Get contents
    @Query("SELECT * FROM contents")
    fun getAllContents(): Flow<List<Content>>

    @Query("SELECT * FROM contents WHERE id = :id")
    fun getContent(id: Int): Flow<Content>

    // Modifying database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: Content)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContents(contents: List<Content>)

    //@Insert(onConflict = OnConflictStrategy.IGNORE)
    //suspend fun insertNoteAndContent(noteAndContent: NoteAndContent)

    @Update
    suspend fun updateNote(note: Note)

    @Update
    suspend fun updateContent(content: Content)

    @Transaction
    @Update
    suspend fun updateContents(contents: List<Content>)

    //@Update
    //suspend fun updateNoteAndContent(noteAndContent: NoteAndContent)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    suspend fun deleteContent(content: Content)

    @Transaction
    @Delete
    suspend fun deleteContents(contents: List<Content>)

//    @Delete
//    suspend fun deleteNoteAndContent(noteAndContent: NoteAndContent)
}
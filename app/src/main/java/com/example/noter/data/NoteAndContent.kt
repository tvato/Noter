package com.example.noter.data

import androidx.room.Embedded
import androidx.room.Relation

data class NoteAndContent(
    @Embedded val note: Note,
    @Relation(
        parentColumn = "id",
        entityColumn = "noteId"
    )
    val contents: List<Content>
)

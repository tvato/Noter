package com.example.noter.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "contents",
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = ["id"],
            childColumns = ["noteId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Content(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val noteId: Int,
    val text: String,
    val checked: Boolean,
    val offset: Int,
    val line: Int,
    val group: Int
)

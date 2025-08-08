package com.example.noter.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noter.NoterApplication
import com.example.noter.ui.note.NoteViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            HomeScreenViewModel(
                noterApplication().container.notesRepository
            )
        }

        initializer {
            NoteViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                noterApplication().container.notesRepository
            )
        }
    }
}

fun CreationExtras.noterApplication(): NoterApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as NoterApplication)
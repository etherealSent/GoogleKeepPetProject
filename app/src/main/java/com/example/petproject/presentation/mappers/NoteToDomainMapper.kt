package com.example.petproject.presentation.mappers

import androidx.compose.ui.graphics.toArgb
import com.example.petproject.domain.entities.note.Note
import com.example.petproject.presentation.model.NoteUi
import javax.inject.Inject

class NoteToDomainMapper @Inject constructor() : (NoteUi) -> Note {
    override fun invoke(note: NoteUi): Note {
        return Note(
            id = note.id,
            title = note.title,
            content = note.content,
            pinned = note.pinned,
            lastUpdate = note.lastUpdate,
            photoPaths = note.photoPaths,
            isArchived = note.isArchived,
            isDeleted = note.isDeleted,
            position = note.position,
            color = note.color.toArgb()
        )
    }
}
package com.knot.feature.note.dto

import com.knot.feature.note.Note
import kotlinx.serialization.Serializable

@Serializable
data class NotesDto(
    val results: List<NoteDto>,
)

/**
 * Converts a List of [Note] objects to a [NotesDto] object.
 *
 * @return A [NotesDto] object containing the converted Note objects.
 */
fun List<Note>.asNotesDto() =
    NotesDto(results = this.map(Note::asNoteDto))

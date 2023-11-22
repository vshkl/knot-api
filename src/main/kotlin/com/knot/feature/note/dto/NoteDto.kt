package com.knot.feature.note.dto

import com.knot.feature.note.Note
import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: Long,
    val title: String,
    val content: String,
)

/**
 * Converts a [Note] to a [NoteDto].
 *
 * @return The converted [NoteDto] object.
 */
fun Note.asNoteDto() =
    NoteDto(
        id = id,
        title = title,
        content = content
    )

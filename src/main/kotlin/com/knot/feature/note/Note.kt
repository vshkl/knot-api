package com.knot.feature.note

import org.jetbrains.exposed.sql.ResultRow

/**
 * Represents a note that belongs to a user.
 *
 * @property id The unique identifier of the note.
 * @property title The title of the note.
 * @property content The content of the note.
 */
data class Note(
    val id: Long,
    val title: String,
    val content: String,
)

/**
 * Converts a database [ResultRow] into a [Note] object.
 *
 * @return The converted [Note] object.
 */
internal fun ResultRow.asNote(): Note {
    return Note(
        id = this[NoteEntity.id].value,
        title = this[NoteEntity.title],
        content = this[NoteEntity.content],
    )
}

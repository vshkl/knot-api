package com.knot.feature.note

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

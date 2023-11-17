package com.knot.models

/**
 * Represents a note that belongs to a user.
 *
 * @property id The unique identifier of the note.
 * @property userId The unique identifier of the user the note belongs to.
 * @property title The title of the note.
 * @property content The content of the note.
 */
data class Note(
    val id: Int,
    val userId: Int,
    val title: String,
    val content: String,
)

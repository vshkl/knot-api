package com.knot.feature.note.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotesDto(
    val results: List<NoteDto>,
)

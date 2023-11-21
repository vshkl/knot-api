package com.knot.feature.note

import kotlinx.serialization.Serializable

@Serializable
data class NotesOutDto(
    val results: List<NoteOutDto>,
)

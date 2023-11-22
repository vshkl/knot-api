package com.knot.feature.note.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoteDto(
    val id: Long,
    val title: String,
    val content: String,
)

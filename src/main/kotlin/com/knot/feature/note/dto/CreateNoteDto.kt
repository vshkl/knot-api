package com.knot.feature.note.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteDto(
    val title: String,
    val content: String,
)

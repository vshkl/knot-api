package com.knot.feature.note.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoteDto(
    val title: String? = null,
    val content: String? = null,
)

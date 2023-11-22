package com.knot.feature.note

import kotlinx.serialization.Serializable

@Serializable
data class CreateNoteInDto(
    val title: String,
    val content: String,
)
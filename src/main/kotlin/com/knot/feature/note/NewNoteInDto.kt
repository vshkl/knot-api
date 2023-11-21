package com.knot.feature.note

import kotlinx.serialization.Serializable

@Serializable
data class NewNoteInDto(
    val title: String,
    val content: String,
)

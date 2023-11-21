package com.knot.feature.note

import kotlinx.serialization.Serializable

@Serializable
data class NoteOutDto(
    val id: Long,
    val title: String,
    val content: String,
)

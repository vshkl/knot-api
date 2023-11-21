package com.knot.feature.note

import kotlinx.serialization.Serializable

@Serializable
data class UpdateNoteInDto(
    val title: String? = null,
    val content: String? = null,
)

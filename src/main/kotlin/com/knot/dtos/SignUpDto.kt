package com.knot.dtos

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpDto(
    val email: String,
    @SerialName("display-name") val displayName: String,
    val password: String,
)

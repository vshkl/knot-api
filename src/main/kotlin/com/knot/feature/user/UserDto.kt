package com.knot.feature.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    @SerialName("display-name") val displayName: String,
)

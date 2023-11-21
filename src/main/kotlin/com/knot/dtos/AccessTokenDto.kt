package com.knot.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenDto(
    val token: String,
)

package com.knot.feature.auth

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenDto(
    val token: String,
)

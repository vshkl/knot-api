package com.knot.feature.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    val email: String,
    val password: String,
)

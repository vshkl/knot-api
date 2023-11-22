package com.knot.feature.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    val email: String,
    val password: String,
)

package com.knot.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SignInDto(
    val email: String,
    val password: String,
)

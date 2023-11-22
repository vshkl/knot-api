package com.knot.feature.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenIn(
    @SerialName("refresh-token") val refreshToken: String,
)

package com.knot.feature.user.dto

import com.knot.feature.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val email: String,
    @SerialName("display-name") val displayName: String,
)

/**
 * Converts a [User] instance to a [UserDto] instance.
 *
 * @return The converted [UserDto] instance.
 */
fun User.asUserDto() =
    UserDto(
        email = email,
        displayName = displayName,
    )

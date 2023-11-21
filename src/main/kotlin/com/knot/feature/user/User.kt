package com.knot.feature.user

import io.ktor.server.auth.*
import java.io.Serializable

/**
 * Represents a user in the system.
 *
 * @param id The unique identifier of the user.
 * @param email The email address of the user.
 * @param displayName The display name of the user.
 * @param passwordHash The password hash of the user.
 */
data class User(
    val id: Long,
    val email: String,
    val displayName: String,
    val passwordHash: String,
): Serializable, Principal

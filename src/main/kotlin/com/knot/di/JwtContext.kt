package com.knot.di

import com.knot.feature.auth.JwtService
import com.knot.feature.auth.PasswordHasher
import com.knot.feature.auth.PasswordHasherImpl
import io.github.cdimascio.dotenv.dotenv

private val dotenv by lazy { dotenv() }

/**
 * Represents a Context for JWT operations that can be provided where needed via Kotlin Context
 * Receivers API.
 */
data class JwtContext(
    val jwtService: JwtService,
    val passwordHasher: PasswordHasher,
) {

    companion object {

        fun standard() = JwtContext(
            jwtService = JwtService(),
            passwordHasher = PasswordHasherImpl(
                secretKet = dotenv["SECRET_KEY"],
            ),
        )
    }
}

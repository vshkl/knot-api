package com.knot.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.knot.models.User
import io.github.cdimascio.dotenv.dotenv
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class JwtService {

    private val dotenv by lazy { dotenv() }

    private val issuer = "knotServer"
    private val jwtSecret = dotenv["JWT_SECRET"]
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    /**
     * A JWT verifier for verifying the authenticity of JSON Web Tokens.
     *
     * @property verifier The JWTVerifier instance responsible for verifying JWTs.
     */
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    /**
     * Generates a token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated token as a string.
     */
    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withExpiresAt(expiresAt())
        .sign(algorithm)

    private fun expiresAt(): Date =
        LocalDateTime.now()
            .plusDays(5)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .let(Date::from)
}

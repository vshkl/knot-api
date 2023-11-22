package com.knot.feature.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.knot.feature.user.User
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
     * Generates an access token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated token as a string.
     */
    fun generateAccessToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("type", TokenType.ACCESS.name)
        .withExpiresAt(expiresAt(TokenType.ACCESS))
        .sign(algorithm)

    /**
     * Generates a refresh token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated token as a string.
     */
    fun generateRefreshToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("type", TokenType.REFRESH.name)
        .withExpiresAt(expiresAt(TokenType.REFRESH))
        .sign(algorithm)

    private fun expiresAt(tokenType: TokenType): Date =
        when (tokenType) {
            TokenType.ACCESS -> 5L
            TokenType.REFRESH -> 30L
        }.let { expiresIn ->
            LocalDateTime.now()
                .plusDays(expiresIn)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .let(Date::from)
        }

}

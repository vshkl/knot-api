package com.knot.feature.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.knot.feature.user.User
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

private const val ACCESS_TOKEN_EXPIRATION_DAYS = 5L
private const val REFRESH_TOKEN_EXPIRATION_DAYS = 30L

class JwtServiceImpl(
    jwtSecret: String,
) : JwtService {

    private val issuer = "knotServer"
    private val algorithm = Algorithm.HMAC512(jwtSecret)

    override val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun identifyToken(token: String): TokenType {
        return verifier.verify(token)
            .getClaim("type")
            .asString()
            .let(TokenType::valueOf)
    }

    override fun identifyUser(token: String): Long {
        return verifier.verify(token)
            .getClaim("id")
            .asLong()
    }

    override fun generateAccessToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("type", TokenType.ACCESS.name)
        .withExpiresAt(expiresAt(TokenType.ACCESS))
        .sign(algorithm)

    override fun generateRefreshToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("id", user.id)
        .withClaim("type", TokenType.REFRESH.name)
        .withExpiresAt(expiresAt(TokenType.REFRESH))
        .sign(algorithm)

    private fun expiresAt(tokenType: TokenType): Date =
        when (tokenType) {
            TokenType.ACCESS -> ACCESS_TOKEN_EXPIRATION_DAYS
            TokenType.REFRESH -> REFRESH_TOKEN_EXPIRATION_DAYS
        }.let { expiresIn ->
            LocalDateTime.now()
                .plusDays(expiresIn)
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .let(Date::from)
        }
}

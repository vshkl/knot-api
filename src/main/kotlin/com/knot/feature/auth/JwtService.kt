package com.knot.feature.auth

import com.auth0.jwt.JWTVerifier
import com.knot.feature.user.User

/**
 * Interface representing a JWT Service.
 * Provides methods for JWT generation, verification, and user identification.
 */
interface JwtService {

    /**
     * A JWT verifier for verifying the authenticity of JSON Web Tokens.
     *
     * @property verifier The JWTVerifier instance responsible for verifying JWTs.
     * @throws IllegalAccessException
     */
    val verifier: JWTVerifier

    /**
     * Identifies the type of the given token represented by [TokenType].
     *
     * @param token The token to be identified.
     * @return The [TokenType] of the given token.
     * @throws IllegalAccessException
     * @throws com.auth0.jwt.exceptions.JWTVerificationException
     */
    fun identifyToken(token: String): TokenType

    /**
     * Identifies the user based on the provided token.
     *
     * @param token The token used to authenticate the user.
     * @return The ID of the user.
     *
     * @throws IllegalAccessException
     * @throws com.auth0.jwt.exceptions.JWTVerificationException
     */
    fun identifyUser(token: String): Long

    /**
     * Generates an access token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated token as a string.
     * @throws IllegalAccessException
     * @throws com.auth0.jwt.exceptions.JWTCreationException
     */
    fun generateAccessToken(user: User): String

    /**
     * Generates a refresh token for the given user.
     *
     * @param user The user for whom the token is generated.
     * @return The generated token as a string.
     *
     * @throws IllegalAccessException
     * @throws com.auth0.jwt.exceptions.JWTCreationException
     */
    fun generateRefreshToken(user: User): String
}

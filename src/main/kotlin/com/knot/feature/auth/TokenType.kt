package com.knot.feature.auth

/**
 * Enumeration for different token types.
 *
 * This enumeration represents the different types of tokens that can be generated.
 * Each token type corresponds to a specific purpose or functionality.
 * The available token types are:
 * - ACCESS: Represents an access token used for authentication and authorization.
 * - REFRESH: Represents a refresh token used to obtain a new access token when it expires.
 */
enum class TokenType {
    ACCESS,
    REFRESH,
}

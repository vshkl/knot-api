package com.knot.feature.auth

/**
 * This interface defines the contract for a password hasher.
 */
interface PasswordHasher {

    /**
     * Calculates the hash of the given password.
     *
     * @param password The password to be hashed.
     * @return The calculated hash of the password.
     */
    fun hash(password: String): String
}

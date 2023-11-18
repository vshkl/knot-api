package com.knot.repository

import com.knot.models.User

/**
 * Interface defining methods for performing operations on user data.
 */
interface UsersRepository {

    /**
     * Creates a new user with the provided email, display name, and password hash.
     *
     * @param email The email of the user to be created.
     * @param displayName The display name of the user.
     * @param passwordHash The hashed password of the user.
     * @return The created User object if successful, null otherwise.
     */
    suspend fun createUser(
        email: String,
        displayName: String,
        passwordHash: String,
    ): User?

    /**
     * Finds a user with the given ID.
     *
     * @param id The ID of the user to be found.
     * @return The user with the given ID, or null if no user is found.
     */
    suspend fun findUser(id: Long): User?

    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return The found user, or null if no user exists with the given email address.
     */
    suspend fun findUser(email: String): User?
}

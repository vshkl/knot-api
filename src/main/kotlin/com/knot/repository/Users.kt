package com.knot.repository

import com.knot.repository.Users.displayName
import com.knot.repository.Users.email
import com.knot.repository.Users.id
import com.knot.repository.Users.passwordHash
import org.jetbrains.exposed.dao.id.LongIdTable

/**
 * The Users class represents a database table for users of the system.
 *
 * @property id The column representing the user ID. Serves as a primary key.
 * @property email The column representing the user's email address.
 * @property displayName The column representing the user's display name.
 * @property passwordHash The column representing the hashed password.
 */
@Suppress("MemberVisibilityCanBePrivate")
object Users : LongIdTable() {
    val email = varchar("email", 128)
        .uniqueIndex()
    val displayName = varchar("display_name", 256)
    val passwordHash = varchar("password_hash", 64)
}

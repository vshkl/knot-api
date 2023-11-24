package com.knot.feature.user

import com.knot.feature.user.UserEntity.displayName
import com.knot.feature.user.UserEntity.email
import com.knot.feature.user.UserEntity.id
import com.knot.feature.user.UserEntity.passwordHash
import org.jetbrains.exposed.dao.id.LongIdTable

private const val EMAIL_LENGTH = 128
private const val DISPLAY_NAME_LENGTH = 256
private const val PASSWORD_HASH_LENGTH = 64

/**
 * The Users class represents a database table for users of the system.
 *
 * @property id The column representing the user ID. Serves as a primary key.
 * @property email The column representing the user's email address.
 * @property displayName The column representing the user's display name.
 * @property passwordHash The column representing the hashed password.
 */
@Suppress("MemberVisibilityCanBePrivate")
object UserEntity : LongIdTable() {
    val email = varchar("email", EMAIL_LENGTH)
        .uniqueIndex()
    val displayName = varchar("display_name", DISPLAY_NAME_LENGTH)
    val passwordHash = varchar("password_hash", PASSWORD_HASH_LENGTH)

    override val tableName: String
        get() = "Users"
}

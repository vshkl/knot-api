package com.knot.repository

import com.knot.database.DatabaseFactory.dbQuery
import com.knot.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.statements.InsertStatement

class UsersRepositoryImpl : UsersRepository {

    override suspend fun createUser(
        email: String,
        displayName: String,
        passwordHash: String,
    ): User? {
        var statement: InsertStatement<Number>? = null

        dbQuery {
            statement = Users.insert { user ->
                user[Users.email] = email
                user[Users.displayName] = displayName
                user[Users.passwordHash] = passwordHash
            }
        }

        return statement?.resultedValues
            ?.first()
            ?.asUser()
    }

    override suspend fun findUser(id: Long): User? {
        return dbQuery {
            Users
                .select { Users.id.eq(id) }
                .map(ResultRow::asUser)
                .singleOrNull()
        }
    }

    override suspend fun findUser(email: String): User? {
        return dbQuery {
            Users
                .select { Users.email.eq(email) }
                .map(ResultRow::asUser)
                .singleOrNull()
        }
    }
}

/**
 * Converts a database ResultRow object to a User object.
 *
 * @return the converted User object.
 */
internal fun ResultRow.asUser(): User {
    return User(
        id = this[Users.id].value,
        email = this[Users.email],
        displayName = this[Users.displayName],
        passwordHash = this[Users.passwordHash],
    )
}

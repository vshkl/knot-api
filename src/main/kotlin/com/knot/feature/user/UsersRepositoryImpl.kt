package com.knot.feature.user

import com.knot.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UsersRepositoryImpl : UsersRepository {

    override suspend fun createUser(
        email: String,
        displayName: String,
        passwordHash: String,
    ): User? = dbQuery {
        UserEntity
            .insert { user ->
                user[UserEntity.email] = email
                user[UserEntity.displayName] = displayName
                user[UserEntity.passwordHash] = passwordHash
            }
            .resultedValues
            ?.firstOrNull()
            ?.asUser()
    }

    override suspend fun findUser(id: Long): User? = dbQuery {
        UserEntity
            .select { UserEntity.id.eq(id) }
            .singleOrNull()
            ?.asUser()
    }

    override suspend fun findUser(email: String): User? = dbQuery {
        UserEntity
            .select { UserEntity.email.eq(email) }
            .singleOrNull()
            ?.asUser()
    }
}

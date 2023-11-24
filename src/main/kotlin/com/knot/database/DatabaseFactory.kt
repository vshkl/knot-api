package com.knot.database

import com.knot.feature.note.NoteEntity
import com.knot.feature.user.UserEntity
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

private const val POOL_SIZE = 3

/**
 * A factory for managing the database connection and executing database queries.
 */
object DatabaseFactory {

    private val dotenv by lazy { dotenv() }

    /**
     * Initializes the database connection and creates necessary tables.
     */
    fun init() {
        Database.connect(composeHikariDataSource())

        transaction {
            SchemaUtils.create(UserEntity)
            SchemaUtils.create(NoteEntity)
        }
    }

    /**
     * Executes the given block of code inside a suspending database transaction.
     * The block is executed on the IO thread.
     *
     * @param block The block of code that represents the database query to be executed.
     * @return The result of the database query.
     */
    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction {
                block()
            }
        }

    private fun composeHikariDataSource(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = dotenv["JDBC_DRIVER_CLASS"]
            jdbcUrl = dotenv["JDBC_DATABASE_URL"]
            maximumPoolSize = POOL_SIZE
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            dotenv["DB_USER"]?.run {
                username = this
            }
            dotenv["DB_PASSWORD"]?.run {
                password = this
            }
        }
        config.validate()

        return HikariDataSource(config)
    }
}

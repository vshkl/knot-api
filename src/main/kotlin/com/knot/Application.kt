package com.knot

import com.knot.auth.JwtService
import com.knot.auth.hash
import com.knot.database.DatabaseFactory
import com.knot.plugins.configureAuthentication
import com.knot.plugins.configureContentNegotiation
import com.knot.plugins.configureResources
import com.knot.plugins.configureRouting
import com.knot.repository.UsersRepository
import com.knot.repository.UsersRepositoryImpl
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val usersRepository = UsersRepositoryImpl()
    val jwtService = JwtService()

    DatabaseFactory.init()
    configureContentNegotiation()
    configureAuthentication(
        usersRepository = usersRepository,
        jwtService = jwtService,
    )
    configureResources()
    configureRouting(
        usersRepository = usersRepository,
        jwtService = jwtService,
        hashFunction = ::hash,
    )
}

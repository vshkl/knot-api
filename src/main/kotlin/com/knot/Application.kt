package com.knot

import com.knot.database.DatabaseFactory
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.PasswordHasher
import com.knot.feature.note.NotesRepositoryImpl
import com.knot.feature.user.UsersRepositoryImpl
import com.knot.plugins.configureAuthentication
import com.knot.plugins.configureContentNegotiation
import com.knot.plugins.configureResources
import com.knot.plugins.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val usersRepository = UsersRepositoryImpl()
    val notesRepository = NotesRepositoryImpl()
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
        notesRepository = notesRepository,
        jwtService = jwtService,
        hashFunction = PasswordHasher::hash,
    )
}

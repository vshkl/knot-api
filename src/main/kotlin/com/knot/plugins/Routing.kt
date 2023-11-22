package com.knot.plugins

import com.knot.feature.auth.JwtService
import com.knot.feature.auth.authRoutes
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.noteRoutes
import com.knot.feature.user.UsersRepository
import com.knot.feature.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting(
    usersRepository: UsersRepository,
    notesRepository: NotesRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    routing {
        authRoutes(usersRepository, jwtService, hashFunction)
        userRoutes()
        noteRoutes(notesRepository)
    }
}

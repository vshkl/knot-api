package com.knot.plugins

import com.knot.di.JwtContext
import com.knot.di.RepositoriesContext
import com.knot.feature.auth.authRoutes
import com.knot.feature.note.noteRoutes
import com.knot.feature.user.userRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

context(JwtContext, RepositoriesContext)
fun Application.configureRouting() {
    routing {
        authRoutes(usersRepository, jwtService, passwordHasher)
        userRoutes()
        noteRoutes(notesRepository)
    }
}

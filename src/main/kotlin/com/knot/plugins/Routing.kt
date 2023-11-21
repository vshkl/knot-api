package com.knot.plugins

import com.knot.feature.auth.JwtService
import com.knot.feature.auth.authRoutes
import com.knot.feature.user.UsersRepository
import com.knot.feature.user.userRoute
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    routing {
        authRoutes(usersRepository, jwtService, hashFunction)
        userRoute(usersRepository, jwtService, hashFunction)
    }
}

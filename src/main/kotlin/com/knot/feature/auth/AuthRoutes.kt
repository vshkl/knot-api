package com.knot.feature.auth

import com.knot.feature.auth.route.refreshToken
import com.knot.feature.auth.route.signIn
import com.knot.feature.auth.route.signUp
import com.knot.feature.user.UsersRepository
import io.ktor.server.routing.Route

fun Route.authRoutes(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    passwordHasher: PasswordHasher,
) {
    signUp(usersRepository, jwtService, passwordHasher)
    signIn(usersRepository, jwtService, passwordHasher)
    refreshToken(usersRepository, jwtService)
}

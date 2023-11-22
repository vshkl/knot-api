package com.knot.feature.auth

import com.knot.feature.auth.route.refreshToken
import com.knot.feature.auth.route.signIn
import com.knot.feature.auth.route.signUp
import com.knot.feature.user.UsersRepository
import io.ktor.server.routing.Route

fun Route.authRoutes(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    signUp(usersRepository, jwtService, hashFunction)
    signIn(usersRepository, jwtService, hashFunction)
    refreshToken(usersRepository, jwtService)
}

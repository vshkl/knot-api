package com.knot.feature.auth

import com.knot.di.JwtContext
import com.knot.di.RepositoriesContext
import com.knot.feature.auth.route.refreshToken
import com.knot.feature.auth.route.signIn
import com.knot.feature.auth.route.signUp
import io.ktor.server.routing.Route

context(JwtContext, RepositoriesContext)
fun Route.authRoutes() {
    signUp(usersRepository, jwtService, passwordHasher)
    signIn(usersRepository, jwtService, passwordHasher)
    refreshToken(usersRepository, jwtService)
}

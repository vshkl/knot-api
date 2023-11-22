package com.knot.plugins

import com.knot.feature.auth.JwtService
import com.knot.feature.user.UsersRepository
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt

const val JWT_NAME = "jwt"
private const val JWT_REALM = "knot-server"

fun Application.configureAuthentication(
    usersRepository: UsersRepository,
    jwtService: JwtService,
) {
    install(Authentication) {
        jwt(JWT_NAME) {
            verifier(jwtService.verifier)
            realm = JWT_REALM
            validate { jwtCredential ->
                val payload = jwtCredential.payload
                val claim = payload.getClaim("id")
                val userId = claim.asLong()
                return@validate usersRepository.findUser(userId)
            }
        }
    }
}

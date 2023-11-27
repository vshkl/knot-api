package com.knot.plugins

import com.knot.di.JwtContext
import com.knot.di.RepositoriesContext
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt

const val JWT_NAME = "jwt"
private const val JWT_REALM = "knot-server"

context(JwtContext, RepositoriesContext)
fun Application.configureAuthentication() {
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

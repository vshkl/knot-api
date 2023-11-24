package com.knot.feature.auth.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.dto.SignInDto
import com.knot.feature.auth.dto.TokensDto
import com.knot.feature.user.User
import com.knot.feature.user.UsersRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.signIn(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    post<AuthResource.SignIn> {
        result {
            val request: SignInDto = call.receive()
            val user: User = usersRepository.findUser(request.email)
                ?.takeIf { hashFunction(request.password) == it.passwordHash }
                .run { ensureNotNull(this) { NoSuchElementException("User not found") } }

            TokensDto(
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = jwtService.generateRefreshToken(user),
            )
        }.fold({ tokens ->
            call.respond(tokens)
        }, {
            application.log.error("Sign In failed: ${it.localizedMessage}")
            when (it) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, "Malformed request")
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.Unauthorized, "Wrong email or password")
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

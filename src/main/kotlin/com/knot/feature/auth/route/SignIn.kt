package com.knot.feature.auth.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.dto.SignInDto
import com.knot.feature.auth.dto.TokensDto
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
            val request = call.receive<SignInDto>()
            val user = usersRepository.findUser(request.email)
                ?.takeIf { hashFunction(request.password) == it.passwordHash }

            ensureNotNull(user) {
                NoSuchElementException("Wrong email or password")
            }

            return@result TokensDto(
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = jwtService.generateRefreshToken(user),
            )
        }.fold({ tokens ->
            call.respond(tokens)
        }, { error ->
            application.log.error("Sign In failed: ${error.localizedMessage}")

            when (error) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, "Malformed request")
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.Unauthorized, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

package com.knot.feature.auth.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.dto.SignUpDto
import com.knot.feature.auth.dto.TokensDto
import com.knot.feature.user.UsersRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.signUp(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    post<AuthResource.SignUp> {
        result {
            val request = call.receive<SignUpDto>()

            val user = usersRepository.createUser(
                email = request.email,
                displayName = request.displayName,
                passwordHash = hashFunction(request.password),
            ).run {
                ensureNotNull(this) {
                    NoSuchElementException("User not created")
                }
            }

            return@result TokensDto(
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = jwtService.generateRefreshToken(user),
            )
        }.fold({ tokens ->
            call.respond(HttpStatusCode.Created, tokens)
        }, { error ->
            application.log.error("Sign Up failed: ${error.localizedMessage}")

            when (error) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, "Malformed request")
                is RequestValidationException ->
                    call.respond(HttpStatusCode.BadRequest, error.reasons.first())
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

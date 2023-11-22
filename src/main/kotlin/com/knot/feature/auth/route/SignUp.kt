package com.knot.feature.auth.route

import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.dto.SignUpDto
import com.knot.feature.auth.dto.TokensDto
import com.knot.feature.user.User
import com.knot.feature.user.UsersRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.ContentTransformationException
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
        lateinit var signUpDto: SignUpDto

        try {
            signUpDto = call.receive()
        } catch (e: ContentTransformationException) {
            application.log.error("Failed to process request", e)
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        if (signUpDto.email.isBlank() || signUpDto.displayName.isBlank() || signUpDto.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        if (signUpDto.password.length < 8) {
            call.respond(HttpStatusCode.BadRequest, "Password is too short")
        }

        try {
            val newUser: User? = usersRepository.createUser(
                email = signUpDto.email,
                displayName = signUpDto.displayName,
                passwordHash = hashFunction.invoke(signUpDto.password),
            )

            newUser?.run {
                val tokensDto = TokensDto(
                    accessToken = jwtService.generateAccessToken(newUser),
                    refreshToken = jwtService.generateRefreshToken(newUser),
                )
                call.respond(HttpStatusCode.Created, tokensDto)
            } ?: run {
                application.log.error("Failed to create user")
                call.respond(HttpStatusCode.BadRequest, "Failed to create user")
            }
        } catch (e: Throwable) {
            application.log.error("Failed to create user", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to create user")
        }
    }
}
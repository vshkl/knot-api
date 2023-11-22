package com.knot.feature.auth.route

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
import io.ktor.server.request.ContentTransformationException
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
        lateinit var signInDto: SignInDto

        try {
            signInDto = call.receive()
        } catch (e: ContentTransformationException) {
            application.log.error("Failed to process request", e)
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        if (signInDto.email.isBlank() || signInDto.password.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        val passwordHash = hashFunction(signInDto.password)
        try {
            val user: User? = usersRepository.findUser(signInDto.email)
            if (passwordHash == user?.passwordHash) {
                val tokensDto = TokensDto(
                    accessToken = jwtService.generateAccessToken(user),
                    refreshToken = jwtService.generateRefreshToken(user),
                )
                call.respond(tokensDto)
            } else {
                application.log.error("Failed to create user")
                call.respond(HttpStatusCode.Unauthorized)
            }
        } catch (e: Throwable) {
            application.log.error("Failed to create user", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to sign in")
        }
    }
}

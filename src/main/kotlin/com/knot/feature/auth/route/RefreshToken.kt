package com.knot.feature.auth.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.TokenType
import com.knot.feature.auth.dto.RefreshTokenDto
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

fun Route.refreshToken(
    usersRepository: UsersRepository,
    jwtService: JwtService,
) {
    post<AuthResource.RefreshToken> {
        result {
            val request = call.receive<RefreshTokenDto>()
            val tokenType = jwtService.identifyToken(request.refreshToken)
                .takeIf { it == TokenType.REFRESH }
            val user = jwtService.identifyUser(request.refreshToken)
                .let { userId -> usersRepository.findUser(userId) }

            ensureNotNull(tokenType) {
                IllegalArgumentException("Wrong token")
            }
            ensureNotNull(user) {
                IllegalArgumentException("Wrong token or token expired")
            }

            return@result TokensDto(
                accessToken = jwtService.generateAccessToken(user),
                refreshToken = jwtService.generateRefreshToken(user),
            )
        }.fold({ tokens ->
            call.respond(tokens)
        }, { error ->
            application.log.error("Token refresh failed: ${error.localizedMessage}")

            when (error) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, "Malformed request")
                is RequestValidationException ->
                    call.respond(HttpStatusCode.BadRequest, error.reasons.first())
                is IllegalArgumentException ->
                    call.respond(HttpStatusCode.Unauthorized, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

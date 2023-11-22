package com.knot.feature.auth.route

import com.knot.feature.auth.AuthResource
import com.knot.feature.auth.JwtService
import com.knot.feature.auth.TokenType
import com.knot.feature.auth.dto.RefreshTokenDto
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

fun Route.refreshToken(
    usersRepository: UsersRepository,
    jwtService: JwtService,
) {
    post<AuthResource.RefreshToken> {
        lateinit var refreshTokenDto: RefreshTokenDto

        try {
            refreshTokenDto = call.receive()
        } catch (e: ContentTransformationException) {
            application.log.error("Failed to process request", e)
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        try {
            val tokenType = jwtService.identifyToken(refreshTokenDto.refreshToken)
            when (tokenType) {
                TokenType.ACCESS -> {
                    application.log.error("Wrong token type")
                    call.respond(HttpStatusCode.Unauthorized)
                }

                TokenType.REFRESH -> {
                    val userId = jwtService.identifyUser(refreshTokenDto.refreshToken)
                    val user: User? = usersRepository.findUser(userId)

                    if (user != null) {
                        val tokensDto = TokensDto(
                            accessToken = jwtService.generateAccessToken(user),
                            refreshToken = jwtService.generateRefreshToken(user),
                        )
                        call.respond(tokensDto)
                    } else {
                        application.log.error("Failed to refresh token")
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }
            }
        } catch (e: Throwable) {
            application.log.error("Failed to refresh token", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to refresh token")
        }
    }
}

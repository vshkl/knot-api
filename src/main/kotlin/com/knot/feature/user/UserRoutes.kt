package com.knot.feature.user

import com.knot.feature.auth.JwtService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    authenticate("jwt") {
        get<UserResource> {
            val user: User? = call.principal<User>()

            user?.run {
                val userDto = UserDto(
                    email = email,
                    displayName = displayName
                )
                call.respond(userDto)
            } ?: run {
                application.log.error("Can't find user")
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

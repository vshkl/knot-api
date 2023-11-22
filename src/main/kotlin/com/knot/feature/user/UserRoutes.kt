package com.knot.feature.user

import com.knot.feature.user.dto.asUserDto
import com.knot.plugins.JWT_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoute() {
    authenticate(JWT_NAME) {
        get<UserResource> {
            val user: User? = call.principal()

            user?.run {
                call.respond(user.asUserDto())
            } ?: run {
                application.log.error("Can't find user")
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}

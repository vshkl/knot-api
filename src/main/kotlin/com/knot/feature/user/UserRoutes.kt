package com.knot.feature.user

import com.knot.feature.user.dto.asUserDto
import com.knot.plugins.JWT_NAME
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.userRoutes() {
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

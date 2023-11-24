package com.knot.feature.user.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.user.User
import com.knot.feature.user.UserResource
import com.knot.feature.user.dto.asUserDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.readUser() {
    get<UserResource> {
        result {
            val user = call.principal<User>()
            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            return@result user.asUserDto()
        }.fold({ user ->
            call.respond(user)
        }, { error ->
            application.log.error("User read failed: ${error.localizedMessage}")

            when (error) {
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

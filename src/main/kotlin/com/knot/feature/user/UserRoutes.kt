package com.knot.feature.user

import com.knot.feature.user.route.readUser
import com.knot.plugins.JWT_NAME
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route

fun Route.userRoutes() {
    authenticate(JWT_NAME) {
        readUser()
    }
}

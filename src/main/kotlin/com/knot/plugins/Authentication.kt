package com.knot.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*

fun Application.configureAuthentication() {
    install(Authentication) {
        // TODO: Configure exact authentication – JWT & Bearer token
    }
}

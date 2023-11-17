package com.knot

import com.knot.database.DatabaseFactory
import com.knot.plugins.configureAuthentication
import com.knot.plugins.configureContentNegotiation
import com.knot.plugins.configureRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseFactory.init()
    configureContentNegotiation()
    configureAuthentication()
    configureRouting()
}

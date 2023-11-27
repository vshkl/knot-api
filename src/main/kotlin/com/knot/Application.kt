package com.knot

import com.knot.database.DatabaseFactory
import com.knot.di.JwtContext
import com.knot.di.RepositoriesContext
import com.knot.plugins.configureAuthentication
import com.knot.plugins.configureContentNegotiation
import com.knot.plugins.configureRequestValidation
import com.knot.plugins.configureResources
import com.knot.plugins.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    with(JwtContext.standard()) {
        with(RepositoriesContext.standard()) {
            DatabaseFactory.init()
            configureContentNegotiation()
            configureRequestValidation()
            configureAuthentication()
            configureResources()
            configureRouting()
        }
    }
}

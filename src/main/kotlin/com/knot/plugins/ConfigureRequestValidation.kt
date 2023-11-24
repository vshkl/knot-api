package com.knot.plugins

import com.knot.feature.auth.authValidations
import com.knot.feature.note.noteValidations
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        authValidations()
        noteValidations()
    }
}

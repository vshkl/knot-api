package com.knot.feature.note.route

import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.deleteNote(notesRepository: NotesRepository) {
    delete<NoteResource.Id> { noteWithId ->
        result {
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            return@result notesRepository.deleteNote(
                id = noteWithId.id,
                userId = user.id,
            ).run { ensure(this) { NoSuchElementException("No note deleted") } }
        }.fold({
            call.respond(HttpStatusCode.OK)
        }, { error ->
            application.log.error("Note delete failed: ${error.localizedMessage}")

            when (error) {
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, error.localizedMessage)
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.BadRequest, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

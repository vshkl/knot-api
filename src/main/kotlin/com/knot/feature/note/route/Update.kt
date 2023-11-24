package com.knot.feature.note.route

import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.UpdateNoteDto
import com.knot.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.request.receive
import io.ktor.server.resources.patch
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.updateNote(notesRepository: NotesRepository) {
    patch<NoteResource.Id> { noteWithId ->
        result {
            val request = call.receive<UpdateNoteDto>()
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            return@result notesRepository.updateNote(
                userId = user.id,
                id = noteWithId.id,
                title = request.title,
                content = request.content,
            ).run { ensure(this) { NoSuchElementException("No note updated") } }
        }.fold({
            call.respond(HttpStatusCode.OK)
        }, { error ->
            application.log.error("Note update failed: ${error.localizedMessage}")

            when (error) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, "Malformed request")
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, error.localizedMessage)
                is RequestValidationException ->
                    call.respond(HttpStatusCode.BadRequest, error.reasons.first())
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.BadRequest, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

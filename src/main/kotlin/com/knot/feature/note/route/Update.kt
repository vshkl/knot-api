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
import io.ktor.server.request.receive
import io.ktor.server.resources.patch
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

private const val MAX_TITLE_LENGTH = 80
private const val MAX_CONTENT_LENGTH = 1000

fun Route.updateNote(notesRepository: NotesRepository) {
    patch<NoteResource.Id> { noteWithId ->
        result {
            val request = call.receive<UpdateNoteDto>()
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }
            ensure((request.title?.length ?: 0) <= MAX_TITLE_LENGTH) {
                IllegalArgumentException("Note title can not be more that $MAX_TITLE_LENGTH long")
            }
            ensure((request.content?.length ?: 0) <= MAX_CONTENT_LENGTH) {
                IllegalArgumentException("Note content can not be more that $MAX_CONTENT_LENGTH long")
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
                is IllegalArgumentException ->
                    call.respond(HttpStatusCode.BadRequest, error.localizedMessage)
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.BadRequest, error.localizedMessage)
                else ->
                    call.respond(HttpStatusCode.InternalServerError, "Unknown error")
            }
        })
    }
}

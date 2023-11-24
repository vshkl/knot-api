package com.knot.feature.note.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.common.dto.ApiResponse
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.CreateNoteDto
import com.knot.feature.note.dto.asNoteDto
import com.knot.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.createNote(notesRepository: NotesRepository) {
    post<NoteResource> {
        result {
            val request = call.receive<CreateNoteDto>()
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            val note = notesRepository.createNote(
                userId = user.id,
                title = request.title,
                content = request.content,
            ).run {
                ensureNotNull(this) {
                    NoSuchElementException("Note not created")
                }
            }

            return@result note.asNoteDto()
        }.fold({ note ->
            call.respond(HttpStatusCode.Created, ApiResponse.Success(note))
        }, { error ->
            application.log.error("Note create failed: ${error.localizedMessage}")

            when (error) {
                is BadRequestException ->
                    call.respond(HttpStatusCode.BadRequest, ApiResponse.Error("Malformed request"))
                is RequestValidationException ->
                    call.respond(HttpStatusCode.BadRequest, ApiResponse.Error(error.reasons.first()))
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, ApiResponse.Error(error.localizedMessage))
                else ->
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse.Error("Unknown error"))
            }
        })
    }
}

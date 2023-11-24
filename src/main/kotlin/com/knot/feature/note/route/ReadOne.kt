package com.knot.feature.note.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.common.dto.ApiResponse
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.asNoteDto
import com.knot.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.readNote(notesRepository: NotesRepository) {
    get<NoteResource.Id> { noteWithId ->
        result {
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            val note = notesRepository.readNote(
                userId = user.id,
                id = noteWithId.id,
            ).run {
                ensureNotNull(this) {
                    NoSuchElementException("Note not found")
                }
            }

            return@result note.asNoteDto()
        }.fold({ note ->
            call.respond(ApiResponse.Success(note))
        }, { error ->
            application.log.error("Note read failed: ${error.localizedMessage}")

            when (error) {
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, ApiResponse.Error(error.localizedMessage))
                is NoSuchElementException ->
                    call.respond(HttpStatusCode.NotFound, ApiResponse.Error(error.localizedMessage))
                else ->
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse.Error("Unknown error"))
            }
        })
    }
}

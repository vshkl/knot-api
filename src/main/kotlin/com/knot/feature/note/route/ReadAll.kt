package com.knot.feature.note.route

import arrow.core.raise.ensureNotNull
import arrow.core.raise.result
import com.knot.common.dto.ApiResponse
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.asNotesDto
import com.knot.feature.user.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.principal
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.readNotes(notesRepository: NotesRepository) {
    get<NoteResource> { notesQuery ->
        result {
            val user = call.principal<User>()

            ensureNotNull(user) {
                IllegalAccessException("Unauthorized")
            }

            val notes = notesRepository.readNotes(
                userId = user.id,
                limit = notesQuery.limit,
                before = notesQuery.before,
                after = notesQuery.after,
                including = notesQuery.including,
            )

            return@result notes.asNotesDto()
        }.fold({ notes ->
            call.respond(ApiResponse.Success(notes))
        }, { error ->
            application.log.error("Notes read failed: ${error.localizedMessage}")

            when (error) {
                is IllegalAccessException ->
                    call.respond(HttpStatusCode.Unauthorized, ApiResponse.Error(error.localizedMessage))
                else ->
                    call.respond(HttpStatusCode.InternalServerError, ApiResponse.Error("Unknown error"))
            }
        })
    }
}

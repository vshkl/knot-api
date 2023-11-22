package com.knot.feature.note.route

import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.NotesDto
import com.knot.feature.note.dto.asNotesDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.readNotes(notesRepository: NotesRepository) {
    get<NoteResource> { notesQuery ->
        try {
            val notesDto: NotesDto = notesRepository.readNotes(
                limit = notesQuery.limit,
                before = notesQuery.before,
                after = notesQuery.after,
                including = notesQuery.including,
            ).asNotesDto()
            call.respond(notesDto)
        } catch (e: Throwable) {
            application.log.error("Failed to find notes", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to find notes")
        }
    }
}

package com.knot.feature.note.route

import com.knot.feature.note.Note
import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.asNoteDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.readNote(notesRepository: NotesRepository) {
    get<NoteResource.Id> { noteWithId ->
        try {
            val note: Note? = notesRepository.findNote(id = noteWithId.id)

            if (note != null) {
                call.respond(note.asNoteDto())
            }

            call.respond(HttpStatusCode.NotFound)
        } catch (e: Throwable) {
            application.log.error("Failed to find note", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to find note")
        }
    }
}

package com.knot.feature.note.route

import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.resources.delete
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.deleteNote(notesRepository: NotesRepository) {
    delete<NoteResource.Id> { noteWithId ->
        try {
            val deleted = notesRepository.deleteNote(id = noteWithId.id)

            if (deleted) {
                call.respond(io.ktor.http.HttpStatusCode.OK)
            } else {
                application.log.error("Failed to delete note")
                call.respond(io.ktor.http.HttpStatusCode.BadRequest, "Failed to delete note")
            }
        } catch (e: Throwable) {
            application.log.error("Failed to delete note", e)
            call.respond(io.ktor.http.HttpStatusCode.BadRequest, "Failed to delete note")
        }
    }
}

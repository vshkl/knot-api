package com.knot.feature.note.route

import com.knot.feature.note.NoteResource
import com.knot.feature.note.NotesRepository
import com.knot.feature.note.dto.UpdateNoteDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.resources.patch
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.updateNote(notesRepository: NotesRepository) {
    patch<NoteResource.Id> { noteWithId ->
        lateinit var updateNoteDto: UpdateNoteDto

        try {
            updateNoteDto = call.receive()
        } catch (e: ContentTransformationException) {
            application.log.error("Failed to process request", e)
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        try {
            val updated = notesRepository.updateNote(
                id = noteWithId.id,
                title = updateNoteDto.title,
                content = updateNoteDto.content,
            )

            if (updated) {
                call.respond(HttpStatusCode.OK)
            } else {
                application.log.error("Failed to update note")
                call.respond(HttpStatusCode.BadRequest, "Failed to update note")
            }
        } catch (e: Throwable) {
            application.log.error("Failed to update note", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to update note")
        }
    }
}

package com.knot.feature.note.route

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
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.createNote(notesRepository: NotesRepository) {
    post<NoteResource> {
        val user: User? = call.principal()
        lateinit var createNoteDto: CreateNoteDto
        try {
            createNoteDto = call.receive()
        } catch (e: ContentTransformationException) {
            application.log.error("Failed to process request", e)
            call.respond(HttpStatusCode.BadRequest, "Incomplete data")
        }

        if (createNoteDto.title.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Note should have title")
        }

        if (createNoteDto.content.isBlank()) {
            call.respond(HttpStatusCode.BadRequest, "Note should have content")
        }

        try {
            user?.id?.let { userId ->
                notesRepository.createNote(
                    userId = userId,
                    title = createNoteDto.title,
                    content = createNoteDto.content,
                )
            }?.run {
                call.respond(HttpStatusCode.Created, this.asNoteDto())
            } ?: run {
                application.log.error("Failed to create note")
                call.respond(HttpStatusCode.BadRequest, "Failed to create note")
            }
        } catch (e: Throwable) {
            application.log.error("Failed to create note", e)
            call.respond(HttpStatusCode.BadRequest, "Failed to create note")
        }
    }
}

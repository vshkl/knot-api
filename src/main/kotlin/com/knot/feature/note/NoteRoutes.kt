package com.knot.feature.note

import com.knot.feature.note.dto.CreateNoteDto
import com.knot.feature.note.dto.NotesDto
import com.knot.feature.note.dto.UpdateNoteDto
import com.knot.feature.note.dto.asNoteDto
import com.knot.feature.note.dto.asNotesDto
import com.knot.feature.user.User
import com.knot.plugins.JWT_NAME
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.resources.delete
import io.ktor.server.resources.get
import io.ktor.server.resources.patch
import io.ktor.server.resources.post
import io.ktor.server.response.respond
import io.ktor.server.routing.Route

fun Route.noteRoutes(
    notesRepository: NotesRepository,
) {
    authenticate(JWT_NAME) {
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
        delete<NoteResource.Id> { noteWithId ->
            try {
                val deleted = notesRepository.deleteNote(id = noteWithId.id)

                if (deleted) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    application.log.error("Failed to delete note")
                    call.respond(HttpStatusCode.BadRequest, "Failed to delete note")
                }
            } catch (e: Throwable) {
                application.log.error("Failed to delete note", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to delete note")
            }
        }
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
}

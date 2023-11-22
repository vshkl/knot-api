package com.knot.feature.note

import com.knot.feature.note.dto.*
import com.knot.feature.user.User
import com.knot.plugins.JWT_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.patch
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(
    notesRepository: NotesRepository,
) {
    authenticate(JWT_NAME) {
        post<NoteResource> {
            val user: User? = call.principal<User>()
            lateinit var createNoteDto: CreateNoteDto

            try {
                createNoteDto = call.receive<CreateNoteDto>()
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
                    val noteDto = NoteDto(id = id, title = title, content = content)
                    call.respond(HttpStatusCode.Created, noteDto)
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
                val noteDtoLists: List<NoteDto> = notesRepository.readNotes(
                    limit = notesQuery.limit,
                    before = notesQuery.before,
                    after = notesQuery.after,
                    including = notesQuery.including,
                ).map { note ->
                    NoteDto(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                    )
                }
                val notesDto = NotesDto(results = noteDtoLists)
                call.respond(notesDto)
            } catch (e: Throwable) {
                application.log.error("Failed to find notes", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to find notes")
            }
        }
        patch<NoteResource.Id> { noteWithId ->
            lateinit var updateNoteDto: UpdateNoteDto

            try {
                updateNoteDto = call.receive<UpdateNoteDto>()
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
                    val noteDto = NoteDto(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                    )
                    call.respond(noteDto)
                }

                call.respond(HttpStatusCode.NotFound)
            } catch (e: Throwable) {
                application.log.error("Failed to find note", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to find note")
            }
        }
    }
}

package com.knot.feature.note

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
            lateinit var createNoteInDto: CreateNoteInDto

            try {
                createNoteInDto = call.receive<CreateNoteInDto>()
            } catch (e: ContentTransformationException) {
                application.log.error("Failed to process request", e)
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            if (createNoteInDto.title.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Note should have title")
            }

            if (createNoteInDto.content.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Note should have content")
            }

            try {
                user?.id?.let { userId ->
                    notesRepository.createNote(
                        userId = userId,
                        title = createNoteInDto.title,
                        content = createNoteInDto.content,
                    )
                }?.run {
                    val noteOutDto = NoteOutDto(id = id, title = title, content = content)
                    call.respond(HttpStatusCode.Created, noteOutDto)
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
                val noteOutDtoList: List<NoteOutDto> = notesRepository.readNotes(
                    limit = notesQuery.limit,
                    before = notesQuery.before,
                    after = notesQuery.after,
                    including = notesQuery.including,
                ).map { note ->
                    NoteOutDto(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                    )
                }
                val notesOutDto = NotesOutDto(results = noteOutDtoList)
                call.respond(notesOutDto)
            } catch (e: Throwable) {
                application.log.error("Failed to find notes", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to find notes")
            }
        }
        patch<NoteResource.Id> { noteWithId ->
            lateinit var updateNoteInDto: UpdateNoteInDto

            try {
                updateNoteInDto = call.receive<UpdateNoteInDto>()
            } catch (e: ContentTransformationException) {
                application.log.error("Failed to process request", e)
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            try {
                val updated = notesRepository.updateNote(
                    id = noteWithId.id,
                    title = updateNoteInDto.title,
                    content = updateNoteInDto.content,
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
                    val noteOutDto = NoteOutDto(
                        id = note.id,
                        title = note.title,
                        content = note.content,
                    )
                    call.respond(noteOutDto)
                }

                call.respond(HttpStatusCode.NotFound)
            } catch (e: Throwable) {
                application.log.error("Failed to find note", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to find note")
            }
        }
    }
}

package com.knot.feature.note

import com.knot.feature.user.User
import com.knot.plugins.JWT_NAME
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.noteRoutes(
    notesRepository: NotesRepository,
) {
    authenticate(JWT_NAME) {
        post<NoteResource> {
            val user: User? = call.principal<User>()
            lateinit var newNoteInDto: NewNoteInDto

            try {
                newNoteInDto = call.receive<NewNoteInDto>()
            } catch (e: ContentTransformationException) {
                application.log.error("Failed to process request", e)
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            if (newNoteInDto.title.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Note should have title")
            }

            if (newNoteInDto.content.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Note should have content")
            }

            try {
                user?.id?.let { userId ->
                    notesRepository.createNote(
                        userId = userId,
                        title = newNoteInDto.title,
                        content = newNoteInDto.content,
                    )
                }?.run {
                    val noteOutDto = NoteOutDto(id = id, title = title, content = content)
                    call.respond(HttpStatusCode.Created, noteOutDto)
                } ?: run {
                    application.log.error("Failed to create user")
                    call.respond(HttpStatusCode.BadRequest, "Failed to create user")
                }
            } catch (e: Throwable) {
                application.log.error("Failed to create note", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to create user")
            }
        }
    }
}

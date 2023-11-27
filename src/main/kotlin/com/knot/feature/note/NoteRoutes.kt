package com.knot.feature.note

import com.knot.di.RepositoriesContext
import com.knot.feature.note.route.createNote
import com.knot.feature.note.route.deleteNote
import com.knot.feature.note.route.readNote
import com.knot.feature.note.route.readNotes
import com.knot.feature.note.route.updateNote
import com.knot.plugins.JWT_NAME
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route

context(RepositoriesContext)
fun Route.noteRoutes() {
    authenticate(JWT_NAME) {
        createNote(notesRepository)
        readNote(notesRepository)
        readNotes(notesRepository)
        updateNote(notesRepository)
        deleteNote(notesRepository)
    }
}

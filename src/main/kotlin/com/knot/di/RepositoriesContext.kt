package com.knot.di

import com.knot.feature.note.NotesRepository
import com.knot.feature.note.NotesRepositoryImpl
import com.knot.feature.user.UsersRepository
import com.knot.feature.user.UsersRepositoryImpl

/**
 * Represents a context containing repositories used in the app that can be provided where needed via Kotlin Context
 * Receivers API.
 */
data class RepositoriesContext(
    val usersRepository: UsersRepository,
    val notesRepository: NotesRepository,
) {

    companion object {
        fun standard() = RepositoriesContext(
            usersRepository = UsersRepositoryImpl(),
            notesRepository = NotesRepositoryImpl(),
        )
    }
}

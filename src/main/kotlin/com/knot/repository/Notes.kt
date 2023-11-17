package com.knot.repository

import com.knot.repository.Notes.content
import com.knot.repository.Notes.title
import com.knot.repository.Notes.userId
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * The Note class represents a database table for storing notes.
 *
 * @property userId The column representing the ID of the user who created the note.
 * @property title The column representing the title of the note.
 * @property content The column representing the content of the note.
 */
@Suppress("MemberVisibilityCanBePrivate")
object Notes : LongIdTable() {
    val userId = long("user_id")
        .references(Users.id, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 80)
    val content = varchar("content", 1000)
}

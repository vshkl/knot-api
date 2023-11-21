package com.knot.feature.note

import com.knot.feature.note.NoteEntity.content
import com.knot.feature.note.NoteEntity.title
import com.knot.feature.note.NoteEntity.userId
import com.knot.feature.user.UserEntity
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
object NoteEntity : LongIdTable() {
    val userId = long("user_id")
        .references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 80)
    val content = varchar("content", 1000)

    override val tableName: String
        get() = "Notes"
}

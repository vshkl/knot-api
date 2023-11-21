package com.knot.feature.note

import com.knot.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NotesRepositoryImpl : NotesRepository {

    override suspend fun createNote(
        userId: Long,
        title: String,
        content: String,
    ): Note? = dbQuery {
        NoteEntity
            .insert { note ->
                note[NoteEntity.userId] = userId
                note[NoteEntity.title] = title
                note[NoteEntity.content] = content
            }
            .resultedValues
            ?.first()
            ?.asNote()
    }

    override suspend fun updateNote(
        id: Long,
        title: String?,
        content: String?,
    ): Boolean = dbQuery {
        NoteEntity
            .update({ NoteEntity.id eq id }) { note ->
                title?.let { note[NoteEntity.title] = it }
                content?.let { note[NoteEntity.content] = it }
            }
    } > 0

    override suspend fun findNote(id: Long): Note? = dbQuery {
        NoteEntity
            .select { NoteEntity.id eq id }
            .singleOrNull()
            ?.asNote()
    }

    override suspend fun deleteNote(id: Long) = dbQuery {
        NoteEntity
            .deleteWhere { NoteEntity.id eq id }
    } > 0
}

/**
 * Converts a database [ResultRow] into a [Note] object.
 *
 * @return The converted [Note] object.
 */
internal fun ResultRow.asNote(): Note {
    return Note(
        id = this[NoteEntity.id].value,
        title = this[NoteEntity.title],
        content = this[NoteEntity.content],
    )
}

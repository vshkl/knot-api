package com.knot.feature.note

import com.knot.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

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
    ): Boolean {
        if (title == null && content == null) {
            return true
        }

        return dbQuery {
            NoteEntity
                .update({ NoteEntity.id eq id }) { note ->
                    title?.let { note[NoteEntity.title] = it }
                    content?.let { note[NoteEntity.content] = it }
                }
        } > 0
    }

    override suspend fun readNote(
        userId: Long,
        id: Long,
    ): Note? = dbQuery {
        NoteEntity
            .select { NoteEntity.userId eq userId and (NoteEntity.id eq id) }
            .singleOrNull()
            ?.asNote()
    }

    override suspend fun readNotes(
        userId: Long,
        limit: Int,
        before: Long?,
        after: Long?,
        including: Boolean,
    ): List<Note> = dbQuery {
        when {
            before != null && after == null -> when (including) {
                true -> NoteEntity.select { NoteEntity.userId eq userId and (NoteEntity.id lessEq before) }
                false -> NoteEntity.select { NoteEntity.userId eq userId and (NoteEntity.id less before) }
            }
                .orderBy(NoteEntity.id, SortOrder.DESC)
                .limit(limit)
                .reversed()
            before == null && after != null -> when (including) {
                true -> NoteEntity.select { NoteEntity.userId eq userId and (NoteEntity.id greaterEq after) }
                false -> NoteEntity.select { NoteEntity.userId eq userId and (NoteEntity.id greater after) }
            }
            else ->
                NoteEntity.select { NoteEntity.userId eq userId }
        }
            .windowed(size = limit, step = limit, partialWindows = true) { window ->
                window.map(ResultRow::asNote)
            }
            .firstOrNull()
            .orEmpty()
    }

    override suspend fun deleteNote(
        id: Long,
        userId: Long,
    ) = dbQuery {
        NoteEntity
            .deleteWhere { NoteEntity.id eq id and (NoteEntity.userId eq userId) }
    } > 0
}

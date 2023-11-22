package com.knot.feature.note

/**
 * Interface defining methods for performing operations on notes data.
 */
interface NotesRepository {

    /**
     * Creates a new note for the given user.
     *
     * @param userId The ID of the user for whom the note is being created.
     * @param title The title of the note.
     * @param content The content of the note.
     * @return The created Note object if successful, null otherwise.
     */
    suspend fun createNote(
        userId: Long,
        title: String,
        content: String,
    ): Note?

    /**
     * Updates a note with the specified ID, title, and content.
     *
     * @param id The ID of the note to update.
     * @param title The new title for the note. Pass null to keep the existing title unchanged.
     * @param content The new content for the note. Pass null to keep the existing content unchanged.
     * @return true if the note was successfully updated, false otherwise.
     */
    suspend fun updateNote(
        id: Long,
        title: String?,
        content: String?,
    ): Boolean

    /**
     * Finds a note by its unique identifier.
     *
     * @param id The identifier of the note to be found.
     * @return The found note, or null if no note is found with the specified identifier.
     */
    suspend fun findNote(id: Long): Note?

    /**
     * Reads notes in paged way.
     *
     * @param limit The maximum number of notes to read. Must be a positive integer.
     * @param before The ID indicating the upper bound for note reading. In backward direction. Can be null.
     * @param after The ID indicating the lower bound for note reading. In forward direction. Can be null.
     * @param including Indicated whether to include cursor item or not.
     * @return A list of Note objects obtained from the source within the specified range.
     */
    suspend fun readNotes(
        limit: Int,
        before: Long?,
        after: Long?,
        including: Boolean,
    ): List<Note>

    /**
     * Deletes a note with the specified ID.
     *
     * @param id The ID of the note to be deleted.
     * @return `true` if the note was successfully deleted, `false` otherwise.
     */
    suspend fun deleteNote(id: Long): Boolean
}

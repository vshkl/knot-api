package com.knot.feature.note

import com.knot.feature.note.dto.CreateNoteDto
import com.knot.feature.note.dto.UpdateNoteDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

private const val MAX_TITLE_LENGTH = 80
private const val MAX_CONTENT_LENGTH = 1000

fun RequestValidationConfig.noteValidations() {
    validate<CreateNoteDto> {
        when {
            it.title.isBlank() ->
                ValidationResult.Invalid("Note title can not be empty")
            it.title.length > MAX_TITLE_LENGTH ->
                ValidationResult.Invalid("Note title can not be more than $MAX_TITLE_LENGTH characters long")
            it.content.isBlank() ->
                ValidationResult.Invalid("Note content can not be empty")
            it.content.length > MAX_CONTENT_LENGTH ->
                ValidationResult.Invalid("Note content can not be more than $MAX_CONTENT_LENGTH characters long")
            else ->
                ValidationResult.Valid
        }
    }
    validate<UpdateNoteDto> {
        when {
            (it.title?.length ?: 0) > MAX_TITLE_LENGTH ->
                ValidationResult.Invalid("Note title can not be more than $MAX_TITLE_LENGTH characters long")
            (it.content?.length ?: 0) > MAX_CONTENT_LENGTH ->
                ValidationResult.Invalid("Note content can not be more than $MAX_CONTENT_LENGTH characters long")
            else ->
                ValidationResult.Valid
        }
    }
}

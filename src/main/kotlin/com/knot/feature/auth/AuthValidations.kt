package com.knot.feature.auth

import com.knot.feature.auth.dto.RefreshTokenDto
import com.knot.feature.auth.dto.SignInDto
import com.knot.feature.auth.dto.SignUpDto
import io.ktor.server.plugins.requestvalidation.RequestValidationConfig
import io.ktor.server.plugins.requestvalidation.ValidationResult

private const val MAX_EMAIL_LENGTH = 128
private const val MAX_DISPLAY_NAME_LENGTH = 256
private const val MIN_PASSWORD_LENGTH = 8

fun RequestValidationConfig.authValidations() {
    validate<RefreshTokenDto> {
        when {
            it.refreshToken.isBlank() ->
                ValidationResult.Invalid("Refresh token can not be empty")
            else -> ValidationResult.Valid
        }
    }
    validate<SignInDto> {
        when {
            it.email.isBlank() ->
                ValidationResult.Invalid("Email can not be empty")
            it.password.isBlank() ->
                ValidationResult.Invalid("Password can not be empty")
            else -> ValidationResult.Valid
        }
    }
    validate<SignUpDto> {
        when {
            it.email.isBlank() ->
                ValidationResult.Invalid("Email can not be empty")
            it.email.length > MAX_EMAIL_LENGTH ->
                ValidationResult.Invalid("Email can not be more than $MAX_EMAIL_LENGTH characters long")
            it.displayName.isBlank() ->
                ValidationResult.Invalid("Display name can not be empty")
            it.displayName.length > MAX_DISPLAY_NAME_LENGTH ->
                ValidationResult.Invalid("Display name can not be more than $MAX_DISPLAY_NAME_LENGTH characters long")
            it.password.isBlank() ->
                ValidationResult.Invalid("Password can not be empty")
            it.password.length < MIN_PASSWORD_LENGTH ->
                ValidationResult.Invalid("Password should be at least $MIN_PASSWORD_LENGTH characters long")
            else ->
                ValidationResult.Valid
        }
    }
}

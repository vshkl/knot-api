package com.knot.common.dto

import kotlinx.serialization.Serializable

/**
 * Represents the response returned from an API call.
 *
 * The ApiResponse class is a sealed class that can have two possible subclasses:
 * - Success: Represents a successful response from the API, containing the data returned.
 * - Error: Represents an error response from the API, containing an error message.
 *
 * @property [T] The type of data returned in a successful response.
 *
 * @see Success
 * @see Error
 */
@Serializable
sealed class ApiResponse {

    /**
     * Represents am unified success response
     *
     * @param T The type of the data in the success response.
     * @property data The data in the success response.
     */
    @Serializable
    data class Success<T>(
        val data: T,
    )

    /**
     * Represents an unified error response.
     *
     * @property message The error message.
     */
    @Serializable
    data class Error(
        val message: String,
    )
}

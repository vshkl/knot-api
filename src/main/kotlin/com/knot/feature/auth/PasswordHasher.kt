package com.knot.feature.auth

import io.ktor.util.hex
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class PasswordHasherImpl(
    private val secretKet: String,
) : PasswordHasher {

    private val key by lazy { SecretKeySpec(hex(secretKet), ALGORITHM) }
    private val mac by lazy {
        Mac.getInstance(ALGORITHM)
            .apply { init(key) }
    }

    override fun hash(password: String): String {
        return mac
            .doFinal(password.toByteArray(Charsets.UTF_8))
            .run(::hex)
    }

    private companion object {
        const val ALGORITHM = "HmacSHA1"
    }
}

/**
 * This interface defines the contract for a password hasher.
 */
interface PasswordHasher {

    /**
     * Calculates the hash of the given password.
     *
     * @param password The password to be hashed.
     * @return The calculated hash of the password.
     */
    fun hash(password: String): String
}

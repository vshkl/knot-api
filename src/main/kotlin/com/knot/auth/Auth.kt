package com.knot.auth

import io.github.cdimascio.dotenv.dotenv
import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val dotenv by lazy { dotenv() }

private val hashKey = hex(dotenv["SECRET_KEY"])
private val hmacKey = SecretKeySpec(hashKey, "HmacSHA1")

/**
 * Hashes a given password using the HmacSHA1 algorithm.
 *
 * @param password the password to be hashed
 * @return the hashed password as a hexadecimal string
 */
fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

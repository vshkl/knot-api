package com.knot.feature.auth

import io.ktor.resources.*

@Resource("/auth")
class AuthResource {

    @Resource("sign-up")
    data class SignUp(val parent: AuthResource = AuthResource())

    @Resource("sign-in")
    data class SignIn(val parent: AuthResource = AuthResource())
}

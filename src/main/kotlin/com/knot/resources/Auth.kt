package com.knot.resources

import io.ktor.resources.*

@Resource("/auth")
class Auth {

    @Resource("sign-up")
    data class SignUp(val parent: Auth = Auth())

    @Resource("sign-in")
    data class SignIn(val parent: Auth = Auth())
}

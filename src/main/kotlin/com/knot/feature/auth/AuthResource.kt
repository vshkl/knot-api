package com.knot.feature.auth

import io.ktor.resources.Resource

@Resource("/auth")
class AuthResource {

    @Resource("sign-up")
    data class SignUp(val parent: AuthResource = AuthResource())

    @Resource("sign-in")
    data class SignIn(val parent: AuthResource = AuthResource())

    @Resource("refresh-token")
    data class RefreshToken(val parent: AuthResource = AuthResource())
}

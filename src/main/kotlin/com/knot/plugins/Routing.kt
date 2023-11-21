package com.knot.plugins

import com.knot.feature.auth.JwtService
import com.knot.feature.auth.AccessTokenDto
import com.knot.feature.auth.SignInDto
import com.knot.feature.auth.SignUpDto
import com.knot.feature.user.User
import com.knot.feature.user.UsersRepository
import com.knot.feature.auth.AuthResource
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    usersRepository: UsersRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String,
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        // Auth
        post<AuthResource.SignUp> {
            lateinit var signUpDto: SignUpDto

            try {
                signUpDto = call.receive<SignUpDto>()
            } catch (e: ContentTransformationException) {
                application.log.error("Failed to process request", e)
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            if (signUpDto.email.isBlank() || signUpDto.displayName.isBlank() || signUpDto.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            if (signUpDto.password.length < 8) {
                call.respond(HttpStatusCode.BadRequest, "Password is too short")
            }

            try {
                val newUser: User? = usersRepository.createUser(
                    email = signUpDto.email,
                    displayName = signUpDto.displayName,
                    passwordHash = hashFunction.invoke(signUpDto.password),
                )

                newUser?.run {
                    val accessTokenDto = AccessTokenDto(token = jwtService.generateToken(newUser))
                    call.respond(HttpStatusCode.Created, accessTokenDto)
                } ?: run {
                    application.log.error("Failed to create user")
                    call.respond(HttpStatusCode.BadRequest, "Failed to create user")
                }
            } catch (e: Throwable) {
                application.log.error("Failed to create user", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to create user")
            }
        }
        post<AuthResource.SignIn> {
            lateinit var signInDto: SignInDto

            try {
                signInDto = call.receive<SignInDto>()
            } catch (e: ContentTransformationException) {
                application.log.error("Failed to process request", e)
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            if (signInDto.email.isBlank() || signInDto.password.isBlank()) {
                call.respond(HttpStatusCode.BadRequest, "Incomplete data")
            }

            val passwordHash = hashFunction(signInDto.password)
            try {
                val user: User? = usersRepository.findUser(signInDto.email)
                if (passwordHash == user?.passwordHash) {
                    val accessTokenDto = AccessTokenDto(token = jwtService.generateToken(user))
                    call.respond(accessTokenDto)
                } else {
                    application.log.error("Failed to create user")
                    call.respond(HttpStatusCode.Unauthorized)
                }
            } catch (e: Throwable) {
                application.log.error("Failed to create user", e)
                call.respond(HttpStatusCode.BadRequest, "Failed to sign in")
            }
        }
    }
}

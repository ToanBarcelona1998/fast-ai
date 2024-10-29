package com.example.routes

import com.example.domain.models.BaseResponseSuccessful
import com.example.services.UserService
import com.example.utils.parseErrorToRespond
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/user") {
        get {
            try {
                val principal = call.principal<JWTPrincipal>()
                val claims = principal?.payload
                val id = claims?.getClaim("id")?.asInt()

                val user = userService.getUserById(id)

                call.respond(
                    status = HttpStatusCode.OK,
                    BaseResponseSuccessful(data = user)
                )
            } catch (e: Exception) {
                parseErrorToRespond(e, call)
            }
        }

        post {

        }
    }
}
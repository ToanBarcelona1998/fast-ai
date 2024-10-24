package com.example.routes

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.BaseResponseError
import com.example.domain.models.BaseResponseSuccessful
import com.example.services.UserService
import com.example.utils.parseErrorToRespond
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService) {
    route("/user") {
        get {
            try {

                val id = call.queryParameters["id"]?.toInt()

                val user = userService.getUserById(id)

                call.respond(
                    status = HttpStatusCode.OK,
                    BaseResponseSuccessful(data = user, message = FastAiException.SUCCESSFUL_MESSAGE)
                )
            } catch (e: Exception) {
                parseErrorToRespond(e, call)
            }
        }

        post {

        }
    }
}
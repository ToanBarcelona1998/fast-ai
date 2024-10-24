package com.example.routes

import com.example.domain.models.BaseResponseError
import com.example.domain.models.BaseResponseSuccessful
import com.example.services.UserService
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService){
    route("/user"){
        get ("/{id}") {
            try{
                val user = userService.getUserById(1)

                call.respond(status = HttpStatusCode.OK , BaseResponseSuccessful(data = user, message = ""))
            }catch (_ : Exception){
                call.respond(status = HttpStatusCode.OK , BaseResponseError(data = null, message = "" , code = HttpStatusCode.allStatusCodes.size))
            }
        }

        get("/query?{page}&{limit}") {

        }

        post {
            
        }
    }
}
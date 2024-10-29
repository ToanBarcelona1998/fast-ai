package com.example.routes

import com.example.domain.models.BaseResponseSuccessful
import com.example.services.AuthService
import com.example.utils.parseErrorToRespond
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(authService: AuthService){
    route("/auth"){
        post("/login") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]

            try{
                val loginResponse = authService.login(email = userName!! , password = password!!)

                call.respond(HttpStatusCode.OK , BaseResponseSuccessful(data = loginResponse))
            }catch (e : Exception){
                parseErrorToRespond(e,call)
            }
        }

        post("/register") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]
            try{
                val registerResponse = authService.register(email = userName , password = password)

                call.respond(HttpStatusCode.OK , BaseResponseSuccessful(data = registerResponse))
            }catch (e: Exception){
                parseErrorToRespond(e,call)
            }
        }
    }
}
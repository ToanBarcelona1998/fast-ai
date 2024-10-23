package com.example.routes

import com.example.application.config.JWTConfig
import com.example.domain.exceptions.FastAiException
import com.example.domain.models.BaseResponseError
import com.example.domain.models.BaseResponseSuccessful
import com.example.services.AuthService
import com.example.utils.parseErrorToRespond
import io.ktor.http.*
import io.ktor.server.application.*
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
                authService.login(email = userName!! , password = password!!)

                val accessToken = JWTConfig.makeJWTToken(1)

                call.respond(HttpStatusCode.OK , BaseResponseSuccessful(data = accessToken , message = FastAiException.SUCCESSFUL_MESSAGE))
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

                call.respond(HttpStatusCode.OK , BaseResponseSuccessful(data = registerResponse , message = FastAiException.SUCCESSFUL_MESSAGE))
            }catch (e: Exception){
                parseErrorToRespond(e,call)
            }
        }
    }
}
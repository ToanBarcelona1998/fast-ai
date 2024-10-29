package com.example.routes

import com.example.services.AuthService
import com.example.utils.parseDataToRespond
import com.example.utils.parseErrorToRespond
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.authRoutes(authService: AuthService){
    route("/auth"){
        post("/login") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]

            try{
                val loginResponse = authService.login(email = userName!! , password = password!!)

                call.parseDataToRespond(loginResponse)
            }catch (e : Exception){
                call.parseErrorToRespond(e)
            }
        }

        post("/register") {
            val formData = call.receiveParameters()

            val userName = formData["user_name"]

            val password = formData["password"]
            try{
                val registerResponse = authService.register(email = userName , password = password)

                call.parseDataToRespond(registerResponse)
            }catch (e: Exception){
                call.parseErrorToRespond(e)
            }
        }
    }
}
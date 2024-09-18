package com.example.routes

import com.example.services.UserService
import io.ktor.server.routing.*

fun Route.userRoutes(userService: UserService){
    route("/user/{id}"){
        get {
            val id = call.request.queryParameters["id"]
        }

        post {
            
        }
    }

    route("users"){
        get {

        }
    }
}
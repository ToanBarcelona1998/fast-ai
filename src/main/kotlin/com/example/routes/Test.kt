package com.example.routes

import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.test(){
    route("/"){
        authenticate("auth-jwt") {
            get("test") {
                call.respondText("Hello")
            }
        }
    }
}
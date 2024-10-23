package com.example.routes

import com.example.domain.models.BaseResponseError
import com.example.domain.models.BaseResponseSuccessful
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.test(){
    route("/"){
        authenticate {
            get("test") {
                call.respondText("Hello")
            }
        }
    }
}
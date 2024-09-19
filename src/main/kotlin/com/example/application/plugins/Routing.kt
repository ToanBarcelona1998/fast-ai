package com.example.application.plugins

import com.example.application.config.JWTConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/login") {

            try{
                val token = JWTConfig.makeJWTToken(1)

                call.respond(token)
            }catch (e: Exception){
                call.respond(mapOf("error" to e.message))
            }
        }

        authenticate {
            get("/") {
                call.respond("Hello")
            }
        }


        // Static plugin. Try to access `/static/index.html`
        staticResources("/static" , null , null) {

        }
    }
}

data class Credentials(val email: String, val password: String)

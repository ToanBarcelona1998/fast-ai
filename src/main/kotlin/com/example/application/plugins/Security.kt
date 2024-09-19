package com.example.application.plugins

import com.example.application.config.JWTConfig
import com.example.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureSecurity(userService: UserService) {
    install(Authentication){
        jwt {
            realm = "ktor.io"
            verifier(JWTConfig.verifier)
            validate { jwtCredential ->
                val id = jwtCredential.payload.getClaim("id").asInt()

                if(id != null){
                    val user = userService.getUser(id)

                    if (user != null){
                        JWTPrincipal(jwtCredential.payload)
                    }
                }

                null
            }
        }
    }
}

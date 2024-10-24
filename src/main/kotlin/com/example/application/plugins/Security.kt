package com.example.application.plugins

import com.example.application.config.JWTConfig
import com.example.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    install(Authentication){
        jwt {
            realm = "ktor.io"
            verifier(JWTConfig.verifier)
            validate { jwtCredential ->
                val id = jwtCredential.payload.getClaim("id").asInt()

                val userService by inject<UserService>()

                if(id != null){
                    try{
                        userService.getUserById(id)
                        return@validate JWTPrincipal(jwtCredential.payload)
                    }catch (e : Exception){
                        return@validate null
                    }
                }

                return@validate null
            }
        }
    }
}

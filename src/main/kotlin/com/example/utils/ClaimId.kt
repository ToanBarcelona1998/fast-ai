package com.example.utils

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun claimId(call: RoutingCall) : Int?{
    val principal = call.principal<JWTPrincipal>()
    val claims = principal?.payload
    val id = claims?.getClaim("id")?.asInt()

    return id
}
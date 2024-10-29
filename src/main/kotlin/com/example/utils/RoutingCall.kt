package com.example.utils

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.BaseResponseError
import com.example.domain.models.BaseResponseSuccessful
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend  fun RoutingCall.parseErrorToRespond(e: Exception){
    if(e is FastAiException){
        respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message , code = e.code))
    }else {
        respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message!! , code = FastAiException.UNKNOWN_ERROR_CODE))
    }
}

suspend fun <T> RoutingCall.parseDataToRespond(data : T){
    respond(
        status = HttpStatusCode.OK,
        BaseResponseSuccessful(data = data)
    )
}

fun RoutingCall.claimId() : Int?{
    val principal = principal<JWTPrincipal>()
    val claims = principal?.payload
    val id = claims?.getClaim("id")?.asInt()

    return id
}
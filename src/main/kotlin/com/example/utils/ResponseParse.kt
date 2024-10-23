package com.example.utils

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.BaseResponseError
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend  fun parseErrorToRespond(e: Exception, call: RoutingCall){
    if(e is FastAiException){
        call.respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message , code = e.code))
    }else {
        call.respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message!! , code = FastAiException.UNKNOWN_ERROR_CODE))
    }
}
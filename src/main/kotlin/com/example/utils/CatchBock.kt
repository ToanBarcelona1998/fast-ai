package com.example.utils

import com.example.domain.exceptions.FastAiException
import com.example.domain.models.BaseResponseError
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend  fun <T> catchBlockService(callback : suspend () -> T) : T{
    return try{
        callback()
    }catch (e: Exception) {
        throw FastAiException(FastAiException.DATABASE_ERROR_CODE, e.message!!)
    }catch (e: FastAiException) {
        throw e
    }
}

suspend  fun parseErrorToRespond(e: Exception, call: RoutingCall){
    if(e is FastAiException){
        call.respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message , code = e.code))
    }else {
        call.respond(HttpStatusCode.OK, BaseResponseError(data = null, message = e.message!! , code = FastAiException.UNKNOWN_ERROR_CODE))
    }
}
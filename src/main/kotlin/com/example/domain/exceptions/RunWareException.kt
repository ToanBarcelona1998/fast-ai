package com.example.domain.exceptions
import kotlinx.serialization.*

@Serializable
data class RunWareException(val code : String,val message: String){
    override fun toString(): String {
        return "[$code] $message"
    }
}

@Serializable
data class RunWareExceptions(val errors : List<RunWareException>){
    override fun toString(): String {
        return errors.joinToString(",") { it.toString() }
    }

    fun toBaseException() : FastAiException{
        return FastAiException(FastAiException.RUNWARE_SEND_REQUEST_ERROR_CODE,toString())
    }
}
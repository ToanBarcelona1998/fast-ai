package com.example.utils

import com.example.domain.exceptions.FastAiException

suspend  fun <T> catchBlockService(callback : suspend () -> T) : T{
    return try{
        callback()
    }catch (e: Exception) {
        throw FastAiException(FastAiException.DATABASE_ERROR_CODE, e.message!!)
    }catch (e: FastAiException) {
        throw e
    }
}
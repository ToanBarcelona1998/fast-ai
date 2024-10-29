package com.example.domain.models
import com.example.domain.exceptions.FastAiException
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponseSuccessful<T>(val message: String =  FastAiException.SUCCESSFUL_MESSAGE, val data: T, val code: Int = 200)

@Serializable
data class BaseResponseError<T>(val code : Int , val message: String , val data: T)
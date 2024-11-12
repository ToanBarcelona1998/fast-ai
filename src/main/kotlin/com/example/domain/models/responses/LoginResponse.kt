package com.example.domain.models.responses
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val accessToken : String,val refreshToken : String)

package com.example.domain.models.responses
import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(val accessToken : String)

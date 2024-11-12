package com.example.domain.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(val accessToken : String,val refreshToken : String)
package com.example.domain.models.responses

import com.example.domain.models.entity.User
import kotlinx.serialization.*

@Serializable
data class GetUserResponse(
    val user: User
)